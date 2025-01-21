package faang.school.achievement.handler.project;

import faang.school.achievement.dto.project.ProjectEvent;
import faang.school.achievement.handler.EventHandler;
import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public abstract class ProjectEventHandler implements EventHandler<ProjectEvent> {
    public static final int MAX_ATTEMPTS = 5;

    @Async("taskExecutor")
    @Transactional
    @Retryable(retryFor = {OptimisticLockException.class},
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = 1000, multiplier = 2))
    @Override
    public abstract void handleEvent(ProjectEvent event);

    @Recover
    public void recover(OptimisticLockException e, ProjectEvent event) {
        log.error("All attempts to save changes to the database were unsuccessful. Attempts made: {}",
                MAX_ATTEMPTS, e);
    }
}
