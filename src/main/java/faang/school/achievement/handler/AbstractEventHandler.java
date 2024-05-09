package faang.school.achievement.handler;

import faang.school.achievement.dto.Event;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.AchievementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventHandler<T extends Event> implements EventHandler<T> {

    protected final AchievementService achievementService;
    protected final UserAchievementRepository userAchievementRepository;
    protected final AchievementProgressRepository achievementProgressRepository;

    protected abstract boolean isSupportedEventType(T event);
    protected abstract String getAchievementName();

    protected void giveAchievementIfEnoughScore(AchievementProgress progress, Achievement achievement) {
        if (progress.getCurrentPoints() >= achievement.getPoints()) {
            UserAchievement userAchievement = UserAchievement.builder()
                    .userId(progress.getUserId())
                    .achievement(achievement)
                    .build();
            achievementService.giveAchievement(userAchievement);
        }
    }

    @Override
    public boolean canHandle(T event) {
        return isSupportedEventType(event);
    }

    @Override
    @Async
    @Retryable(value = {OptimisticLockingFailureException.class}, maxAttempts = 4, backoff = @Backoff(delay = 1000))
    public void handleEvent(T event) {

        Achievement achievement = null;
        try {
            achievement = achievementService.getAchievementByName(getAchievementName());
        } catch (EntityNotFoundException exception) {
            log.warn(exception.getMessage());
        }

        long userId = event.getUserId();
        long achievementId = achievement.getId();

        if (achievementService.hasAchievement(userId, achievementId)) {
            log.info("User has already received achievement: {}", getAchievementName());
            return;
        }

        achievementService.createProgressIfNecessary(userId, achievementId);
        AchievementProgress progress = achievementService.getProgress(userId, achievementId).get();
        achievementService.incrementAchievementProgress(progress);
        achievementService.updateAchievementProgress(progress);
        giveAchievementIfEnoughScore(progress, achievement);
    }
}
