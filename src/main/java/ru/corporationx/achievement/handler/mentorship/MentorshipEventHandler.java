package ru.corporationx.achievement.handler.mentorship;

import ru.corporationx.achievement.dto.mentorship.MentorshipStartEvent;
import ru.corporationx.achievement.handler.EventHandler;
import jakarta.persistence.OptimisticLockException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public abstract class MentorshipEventHandler implements EventHandler<MentorshipStartEvent> {

    @Async("taskExecutor")
    @Transactional
    @Retryable(retryFor = {OptimisticLockException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2))
    public abstract void handleEvent(MentorshipStartEvent event);
}
