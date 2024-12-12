package faang.school.achievement.config;

import faang.school.achievement.message.consumer.MentorshipEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final MentorshipEventListener mentorshipEventListener;

    @Value("${spring.data.redis.channel.mentorship}")
    private String mentorshipEventTopicName;

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.create(redisConnectionFactory);
    }

    @Bean
    public Cache cache(RedisCacheManager redisCacheManager) {
        return redisCacheManager.getCache("achievementCache");
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(redisConnectionFactory);

        ChannelTopic mentorshipEventTopic = new ChannelTopic(mentorshipEventTopicName);
        MessageListenerAdapter mentorshipListenerAdapter = new MessageListenerAdapter(mentorshipEventListener);
        listenerContainer.addMessageListener(mentorshipListenerAdapter, mentorshipEventTopic);

        return listenerContainer;
    }
}
