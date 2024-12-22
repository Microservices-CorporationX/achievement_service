package faang.school.achievement.utils;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AsyncEventHandlerProcessor {
    private final AchievementService achievementService;

    @Async("taskExecutor")
    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttemptsExpression = "${retryable.max-attempts}",
            backoff = @Backoff(delayExpression = "${retryable.delay}"))
    public void process(Long userId, String title) {
        Achievement achievement = achievementService.getAchievementByTitle(title);

        if (!achievementService.hasAchievement(userId, achievement.getId())) {
            Long achievementId = achievement.getId();
            achievementService.createProgressIfNecessary(userId, achievementId);
            AchievementProgress progress = achievementService.getProgress(userId, achievementId);
            progress.setUpdatedAt(LocalDateTime.now());
            progress.increment();
            achievementService.updateProgress(progress);

            if (progress.getCurrentPoints() >= achievement.getPoints()) {
                UserAchievement userAchievement = UserAchievement.builder()
                        .achievement(achievement)
                        .userId(progress.getUserId())
                        .build();
                achievementService.giveAchievement(userAchievement);
            }
        }
    }
}
