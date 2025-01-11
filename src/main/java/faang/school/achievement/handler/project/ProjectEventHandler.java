package faang.school.achievement.handler.project;

import faang.school.achievement.dto.project.ProjectEvent;
import faang.school.achievement.handler.EventHandler;
import jakarta.persistence.OptimisticLockException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public abstract class ProjectEventHandler implements EventHandler<ProjectEvent> {
    @Async("taskExecutor")
    @Transactional
    @Retryable(retryFor = {OptimisticLockException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2))
    @Override
    public abstract void handleEvent(ProjectEvent event);
}
