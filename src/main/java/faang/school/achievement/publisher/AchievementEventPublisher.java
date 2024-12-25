package faang.school.achievement.publisher;

import faang.school.achievement.config.redis.RedisConfigProperties;
import faang.school.achievement.event.AchievementEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class AchievementEventPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisConfigProperties redisConfigProperties;

    @Retryable(interceptor = "retryInterceptor")
    public void publish(AchievementEvent event) {
        try {
            redisTemplate.convertAndSend(redisConfigProperties.channel().achievement(), event);
            log.info("Published achievement event: {}", event);
        } catch (Exception e) {
            log.error("Failed to publish achievement event: {}", e.getMessage());
        }
    }
}
