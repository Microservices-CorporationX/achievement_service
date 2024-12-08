package faang.school.achievement.event_handler.impl.project;

import faang.school.achievement.event.project.ProjectEvent;
import faang.school.achievement.event_handler.EventHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.achievement.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public abstract class ProjectAchievementHandler implements EventHandler<ProjectEvent> {

    private final AchievementService achievementService;
    private final AchievementCache achievementCache;

    @Async("achievementHandlingExecutor")
    public void handleAchievement(String achievementName, ProjectEvent event) {
        Achievement achievement = achievementCache.get(achievementName);

        if (!achievementService.hasAchievement(event.getAuthorId(), event.getProjectId())) {
            achievementService.createProgressIfNecessary(event.getAuthorId(), achievement.getId());
            AchievementProgress achievementProgress =
                    achievementService.getProgress(event.getAuthorId(), achievement.getId());
            achievementProgress.increment();
            achievementService.saveProgress(achievementProgress);

            if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
                UserAchievement userAchievement = UserAchievement.builder()
                        .achievement(achievement)
                        .userId(event.getAuthorId())
                        .build();
                achievementService.giveAchievement(userAchievement);
            }
        }
    }
}
