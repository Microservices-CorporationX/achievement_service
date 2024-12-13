package faang.school.achievement.config.redis;

import faang.school.achievement.listener.ProfilePicEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

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
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60)) // Время жизни кеша
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
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