package faang.school.achievement.handler.impl;

import faang.school.achievement.event.AlbumCreatedEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.Cache;
import faang.school.achievement.service.AchievementService;
import org.springframework.stereotype.Service;

@Service
public class LibrarianAchievementHandler extends AbstractEventHandler<AlbumCreatedEvent> {

    private final String ACHIEVEMENT_TITLE = "LIBRARIAN";

    public LibrarianAchievementHandler(Cache<Achievement> achievementCache,
                                       AchievementService achievementService) {
        super(achievementCache, achievementService);
    }

    @Override
    public void handleEvent(AlbumCreatedEvent event) {
        handleAchievement(event.getUserId(), ACHIEVEMENT_TITLE);
    }
}