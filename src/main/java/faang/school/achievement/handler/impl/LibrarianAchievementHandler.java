package faang.school.achievement.handler.impl;

import faang.school.achievement.event.AlbumCreatedEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.springframework.stereotype.Service;

@Service
public class LibrarianAchievementHandler extends AbstractEventHandler<AlbumCreatedEvent> {

    private final String ACHIEVEMENT_TITLE = "LIBRARIAN";

    public LibrarianAchievementHandler(AchievementCache achievementCache,
                                       AchievementService achievementService) {
        super(achievementCache, achievementService);
    }

    @Override
    public void handleEvent(AlbumCreatedEvent event) {
        handleAchievement(event.getUserId(), ACHIEVEMENT_TITLE);
    }

    @Override
    public Class<AlbumCreatedEvent> getEventClass() {
        return AlbumCreatedEvent.class;
    }
}