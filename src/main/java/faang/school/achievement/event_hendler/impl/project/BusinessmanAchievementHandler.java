package faang.school.achievement.event_hendler.impl.project;

import faang.school.achievement.event.project.ProjectEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.achievement.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusinessmanAchievementHandler extends ProjectEventHandler {

    private final AchievementService achievementService;
    private final AchievementCache achievementCache;

    @Override
    @Async("achievementHandlingExecutor")
    public void handleEvent(ProjectEvent event) {
        long point = 1L;
        Achievement achievement = achievementCache.get("BUSINESSMAN");
        if (!achievementService.hasAchievement(event.getAuthorId(), event.getProjectId())) {
            achievementService.createProgressIfNecessary(event.getAuthorId(), achievement.getId());
            AchievementProgress achievementProgress = achievementService.
                    getProgress(event.getAuthorId(), event.getProjectId());
           achievementProgress.setCurrentPoints(achievementProgress.getCurrentPoints() + point);

           if(achievementProgress.getCurrentPoints() >= achievement.getPoints()){
//                achievementService.giveAchievement();
           }
        }
    }
}
