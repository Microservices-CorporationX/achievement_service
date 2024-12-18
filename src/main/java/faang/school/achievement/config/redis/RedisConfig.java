package faang.school.achievement.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.listener.MentorshipEventListener;
import faang.school.achievement.listener.SkillEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channel.mentorship_channel}")
    private String mentorshipEventTopic;

    @Value("${spring.data.redis.channel.skill_acquired_topic}")
    private String skillEventTopic;

    @Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        serializer.setObjectMapper(objectMapper);
        template.setValueSerializer(serializer);
        return template;
    }

    @Bean
    public ChannelTopic mentorshipEventTopic(){
        return new ChannelTopic(mentorshipEventTopic);
    }

    @Bean
    public ChannelTopic skillEventTopic() {
        return new ChannelTopic(skillEventTopic);
    }

    @Bean
    public MessageListenerAdapter mentorshipEventListenerAdapter(MentorshipEventListener mentorshipEventListener){
        return new MessageListenerAdapter(mentorshipEventListener);
    }

    @Bean
    public MessageListenerAdapter skillEventListenerAdapter(SkillEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public RedisMessageListenerContainer container(JedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter mentorshipEventListenerAdapter,
                                                   MessageListenerAdapter skillEventListenerAdapter,
                                                   ChannelTopic mentorshipEventTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(mentorshipEventListenerAdapter,mentorshipEventTopic);
        container.addMessageListener(skillEventListenerAdapter, skillEventTopic());
        return container;
    }
}
