package faang.school.achievement.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties(
        long port,
        String host,
        Channel channel) {
    public record Channel(
            String achievementChannel,
            String followerChannel,
            String mentorshipAcceptedChannel
    ) {
    }
}
