package faang.school.achievement.publisher;

import faang.school.achievement.config.RedisConfigChannelsProperties;
import faang.school.achievement.event.AchievementEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class AchievementEventPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisConfigChannelsProperties redisConfigProperties;

    @Retryable(retryFor = Exception.class,
            maxAttemptsExpression = "#{@retryProperties.maxAttempts}",
            backoff = @Backoff(
                    delayExpression = "#{@retryProperties.initialDelay}",
                    multiplierExpression = "#{@retryProperties.multiplier}",
                    maxDelayExpression = "#{@retryProperties.maxDelay}"
            )
    )
    public void publish(AchievementEvent event) {
        redisTemplate.convertAndSend(redisConfigProperties.achievement(), event);
        log.info("Published achievement event: {}", event);
    }
}
