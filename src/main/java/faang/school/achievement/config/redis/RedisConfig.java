package faang.school.achievement.config.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.achievement.listener.ConglomerateAchievementEventListener;
import faang.school.achievement.listener.ProfilePicEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ChannelTopic teamChannel() {
        log.info(CREATE_CHANNEL_LOG_MESSAGE, redisProperties.getTeamChannel());
        return new ChannelTopic(redisProperties.getTeamChannel());
    }
    @Bean
    public ChannelTopic profilePicChannel() {
        return new ChannelTopic(redisProperties.getProfilePicChannel());
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
    public RedisMessageListenerContainer container(LettuceConnectionFactory lettuceConnectionFactory,
                                                   MessageListenerAdapter conglomerateAchievementEventListenerAdapter) {
            log.info("Настройка RedisMessageListenerContainer...");
            RedisMessageListenerContainer container = new RedisMessageListenerContainer();
            container.setConnectionFactory(lettuceConnectionFactory);
            container.addMessageListener(conglomerateAchievementEventListenerAdapter, new ChannelTopic(redisProperties.getTeamChannel()));

            log.info("RedisMessageListenerContainer успешно настроен для канала 'team_channel'.");
            return container;
    }

    @Bean
    MessageListenerAdapter conglomerateAchievementEventListenerAdapter(ConglomerateAchievementEventListener conglomerateAchievementEventListener) {
        log.info("Настройка ConglomerateAchievementEventListener для обработки сообщений...");
        return new MessageListenerAdapter(conglomerateAchievementEventListener, "onMessage");
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       MessageListenerAdapter messageListenerAdapter,
                                                                       ChannelTopic profilePicChannel) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter, profilePicChannel);
        return container;
    }
}
