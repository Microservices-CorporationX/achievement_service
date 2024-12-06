package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementProgressService;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public abstract class AbstractAchievementHandler {
    protected final AchievementCache achievementCache;
    protected final AchievementService achievementService;
    protected final AchievementProgressService achievementProgressService;

    @Async("customTaskExecutor")
    public void handleAchievement(Long userId, String titleAchievement) {
        Achievement achievement = getAchievement(titleAchievement);
        if (!achievementService.hasAchievement(userId, achievement.getId())) {
            achievementProgressService.createProgressIfNecessary(userId, achievement.getId());
            AchievementProgress achievementProgress = achievementProgressService.getProgress(userId, achievement.getId());
            achievementProgress.increment();
            achievementProgressService.saveProgress(achievementProgress);
            log.info("User {} have point {} of {} for achievement {}",
                    userId,
                    achievementProgress.getCurrentPoints(),
                    achievement.getPoints(),
                    achievement.getTitle()
            );
            if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
                UserAchievement userAchievement = new UserAchievement();
                userAchievement.setUserId(userId);
                userAchievement.setAchievement(achievement);
                achievementService.giveAchievement(userAchievement);
                log.info("User {} give achievement {}", userId, achievement.getTitle());
            }
        }
    }

    protected Achievement getAchievement(String title) {
        return achievementCache.get(title);
    }

    public abstract Class<?> requiredEvent();
}
