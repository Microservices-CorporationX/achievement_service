package faang.school.achievement.handler.impl.project;

import faang.school.achievement.event.project.ProjectEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.springframework.stereotype.Component;

@Component
public class BusinessmanAchievementHandler extends AbstractEventHandler<ProjectEvent> {

    private final static String ACHIEVEMENT_TITLE = "BUSINESSMAN";

    public BusinessmanAchievementHandler(AchievementService achievementService,
                                         AchievementCache achievementCache) {
        super(achievementCache, achievementService);
    }

    @Override
    public void handleEvent(ProjectEvent event) {
        handleAchievement(event.getAuthorId(), ACHIEVEMENT_TITLE);
    }

    @Override
    public boolean supportsEvent(Class<?> eventType) {
        return ProjectEvent.class.equals(eventType);
    }
}