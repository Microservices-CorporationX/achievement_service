package faang.school.achievement.config.redis;

import faang.school.achievement.listener.MentorshipStartEventListener;
import faang.school.achievement.listener.ProfilePicEventListener;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
@Slf4j
@EnableCaching
public class RedisConfig {

    private final RedisProperties redisProperties;

    private static final String CREATE_CHANNEL_LOG_MESSAGE = "Создание ChannelTopic для канала: {}";

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        log.info("Создание LettuceConnectionFactory для Redis с хостом: {} и портом:{}", redisProperties.getHost(), redisProperties.getPort());
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        log.info("LettuceConnectionFactory успешно создан");
        return connectionFactory;
    }

    @Bean
    public ChannelTopic achievementChannelTopic() {
        log.info(CREATE_CHANNEL_LOG_MESSAGE, redisProperties.getAchievementChannel());
        return new ChannelTopic(redisProperties.getAchievementChannel());
    }

    @Bean
    public ChannelTopic profilePicChannel() {
        return new ChannelTopic(redisProperties.getProfilePicChannel());
    }

    @Bean
    public ChannelTopic mentorshipChannel() {
        return new ChannelTopic(redisProperties.getMentorshipChannel());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("Создание RedisTemplate с кастомными сериализаторами.");

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);

        log.info("RedisTemplate создан и сериализаторы настроены.");
        return redisTemplate;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(ProfilePicEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public MessageListenerAdapter mentorshipStartListenerAdapter(MentorshipStartEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer
            (RedisConnectionFactory connectionFactory,
             MessageListenerAdapter messageListenerAdapter,
             ChannelTopic profilePicChannel,
             MessageListenerAdapter mentorshipStartListenerAdapter,
             ChannelTopic mentorshipChannel
            ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter, profilePicChannel);
        container.addMessageListener(mentorshipStartListenerAdapter, mentorshipChannel);
        return container;
    }
}