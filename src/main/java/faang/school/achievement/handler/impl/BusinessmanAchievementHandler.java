package faang.school.achievement.handler.impl;

import faang.school.achievement.event.ProjectEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.service.achievement.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import org.springframework.stereotype.Component;

@Component
public class BusinessmanAchievementHandler extends AbstractEventHandler<ProjectEvent> {

    private final String ACHIEVEMENT_TITLE = "BUSINESSMAN";

    public BusinessmanAchievementHandler(AchievementService achievementService,
                                         AchievementCache achievementCache) {
        super(achievementCache, achievementService);
    }

    @Override
    public void handleEvent(ProjectEvent event) {
        handleAchievement(event.getAuthorId(), ACHIEVEMENT_TITLE);
    }
}