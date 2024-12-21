package faang.school.achievement.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis.channels")
public record RedisConfigChannelsProperties(String achievement, String follower, String skill) {
}