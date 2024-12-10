package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.event.AlbumCreatedEvent;
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
public abstract class AbstractAchievementHandler implements EventHandler<AlbumCreatedEvent> {

    protected final AchievementCache achievementCache;
    protected final AchievementService achievementService;
    protected final AchievementProgressService achievementProgressService;

    @Async("eventHandlingTaskExecutor")
    public void handleAchievement(AlbumCreatedEvent event, String titleAchievement) {
        Achievement achievement = achievementCache.get(titleAchievement);
        if (!achievementService.hasAchievement(event.getUserId(), achievement.getId())) {
            achievementProgressService.createProgressIfNecessary(event.getUserId(), achievement.getId());
            AchievementProgress achievementProgress = achievementProgressService.getProgress(event.getUserId(), achievement.getId());
            achievementProgress.increment();
            achievementProgressService.saveProgress(achievementProgress);
            if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
                UserAchievement userAchievement = new UserAchievement();
                userAchievement.setUserId(event.getUserId());
                userAchievement.setAchievement(achievement);
                achievementService.giveAchievement(userAchievement);
            }
        }
    }

}
