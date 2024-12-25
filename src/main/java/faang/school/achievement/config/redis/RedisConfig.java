package faang.school.achievement.config.redis;

import faang.school.achievement.listener.mentorship.MentorshipEventListener;
import faang.school.achievement.listener.recommendation.RecommendationEventListener;
import faang.school.achievement.listener.team.TeamEventListener;
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
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.team}")
    private String teamChannel;

    @Value("${spring.data.redis.channel.mentorship}")
    private String mentorshipChannel;

    @Value("${spring.data.redis.channel.recommendation}")
    private String recommendationChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public MessageListenerAdapter teamListener(TeamEventListener teamEventListener) {
        return new MessageListenerAdapter(teamEventListener);
    }

    @Bean
    public ChannelTopic teamTopic() {
        return new ChannelTopic(teamChannel);
    }

    @Bean
    public MessageListenerAdapter mentorshipListener(MentorshipEventListener mentorshipEventListener) {
        return new MessageListenerAdapter(mentorshipEventListener);
    }

    @Bean
    public ChannelTopic mentorshipTopic() {
        return new ChannelTopic(mentorshipChannel);
    }

    @Bean
    public MessageListenerAdapter recommendationListener(RecommendationEventListener recommendationEventListener) {
        return new MessageListenerAdapter(recommendationEventListener);
    }

    @Bean
    public ChannelTopic recommendationTopic() {
        return new ChannelTopic(recommendationChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter teamListener,
                                                        MessageListenerAdapter mentorshipListener,
                                                        MessageListenerAdapter recommendationListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(teamListener, teamTopic());
        container.addMessageListener(mentorshipListener, mentorshipTopic());
        container.addMessageListener(recommendationListener, recommendationTopic());
        return container;
    }
}
