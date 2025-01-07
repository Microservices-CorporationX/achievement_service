package faang.school.achievement.config.context.redis;

import faang.school.achievement.listener.MentorshipAcceptedEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisConfig {
    private final RedisProperties redisProperties;
    private final MentorshipAcceptedEventListener mentorshipAcceptedEventListener;

    @Bean
    LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        log.info("Lettuce client for Redis is configured: host = {}, port = {}", redisProperties.getHost(), redisProperties.getPort());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public ChannelTopic mentorshipAcceptedTopic() {
        return new ChannelTopic(redisProperties.getChannel().getMentorshipAcceptedChannel());
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            LettuceConnectionFactory lettuceConnectionFactory,
            ChannelTopic mentorshipAcceptedTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(lettuceConnectionFactory);
        container.addMessageListener(mentorshipAcceptedEventListener, mentorshipAcceptedTopic);
        return container;
    }
}
