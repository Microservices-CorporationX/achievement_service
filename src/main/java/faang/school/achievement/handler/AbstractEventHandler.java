package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventHandler<T> implements EventHandler<T> {
    private final AchievementCache achievementCache;
    private final AchievementProgressHandler achievementProgressHandler;

    @Async("taskExecutor")
    public void handleAchievement(Long userId, String achievementTitle) {
        try {
            Achievement achievement = achievementCache.getAchievement(achievementTitle);
            achievementProgressHandler.handleAchievementProgress(userId, achievement);
        } catch (Exception e) {
            log.error("Error handling achievement for user id: {}", userId, e);
        }
    }
}
