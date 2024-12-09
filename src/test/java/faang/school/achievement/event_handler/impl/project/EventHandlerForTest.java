package faang.school.achievement.event_handler.impl.project;

import faang.school.achievement.event.project.ProjectEvent;
import faang.school.achievement.service.achievement.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;

public class EventHandlerForTest extends ProjectAchievementHandler {

    public EventHandlerForTest(AchievementService achievementService, AchievementCache achievementCache) {
        super(achievementService, achievementCache);
    }

    @Override
    public void handleEvent(ProjectEvent event) {
        handleAchievement("achievementName",event);
    }
}
