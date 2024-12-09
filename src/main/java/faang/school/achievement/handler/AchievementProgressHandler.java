package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementProgressHandler {
    private final AchievementService achievementService;

    public void handleAchievementProgress(Long userId, Achievement achievement) {
        if (achievementService.hasAchievement(userId, achievement.getId())) {
            log.info("User with id: {} already has achievement with id: {}", userId, achievement.getId());
            return;
        }

        achievementService.createProgressIfNecessary(userId, achievement.getId());
        AchievementProgress progress = achievementService.getProgress(userId, achievement.getId());
        progress.increment();

        if (progress.getCurrentPoints() >= achievement.getPoints()) {
            achievementService.giveAchievement(userId, achievement.getId());
            log.info("User with id: {} received achievement with id: {}", userId, achievement.getId());
        }
        achievementService.updateProgress(progress);
    }
}
