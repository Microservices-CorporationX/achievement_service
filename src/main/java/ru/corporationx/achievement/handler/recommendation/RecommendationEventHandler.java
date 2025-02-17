package ru.corporationx.achievement.handler.recommendation;

import ru.corporationx.achievement.dto.recommendation.RecommendationEvent;
import ru.corporationx.achievement.handler.EventHandler;
import jakarta.persistence.OptimisticLockException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public abstract class RecommendationEventHandler implements EventHandler<RecommendationEvent> {

    @Async("taskExecutor")
    @Transactional
    @Retryable(retryFor = {OptimisticLockException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2))
    public abstract void handleEvent(RecommendationEvent event);
}
