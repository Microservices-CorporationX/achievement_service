package faang.school.achievement.handler.subscription;

import faang.school.achievement.dto.subscription.FollowerEvent;
import faang.school.achievement.dto.team.TeamEvent;
import faang.school.achievement.handler.EventHandler;
import jakarta.persistence.OptimisticLockException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public abstract class FollowerEventHandler implements EventHandler<FollowerEvent> {

    @Async("taskExecutor")
    @Transactional
    @Retryable(retryFor = {OptimisticLockException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2))
    public abstract void handleEvent(FollowerEvent event);
}
