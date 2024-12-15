package faang.school.achievement.config.redis;

import faang.school.achievement.listener.ProfilePicEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableCaching
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        log.info("Создание LettuceConnectionFactory для Redis с хостом: {} и портом:{}", redisProperties.getRedisHost(), redisProperties.getRedisPort());
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getRedisHost(), redisProperties.getRedisPort());
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        log.info("LettuceConnectionFactory успешно создан");
        return connectionFactory;
    }

    @Bean
    public ChannelTopic profilePicChannel() {
        return new ChannelTopic(redisProperties.getProfilePicChannel());
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter messageListenerAdapter,
            ChannelTopic profilePicChannel) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter, profilePicChannel);
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(ProfilePicEventListener listener) {
        return new MessageListenerAdapter(listener);
    }
}