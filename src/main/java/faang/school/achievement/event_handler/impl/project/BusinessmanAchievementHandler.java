package faang.school.achievement.event_handler.impl.project;

import faang.school.achievement.event.project.ProjectEvent;
import faang.school.achievement.service.achievement.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import org.springframework.stereotype.Component;

@Component
public class BusinessmanAchievementHandler extends AbstractProjectAchievementHandler {

    public BusinessmanAchievementHandler(AchievementService achievementService,
                                         AchievementCache achievementCache) {
        super(achievementService, achievementCache);
    }

    @Override
    public void handleEvent(ProjectEvent event) {
        handleAchievement("BUSINESSMAN", event);
    }
}