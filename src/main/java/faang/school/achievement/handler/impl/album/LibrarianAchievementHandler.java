package faang.school.achievement.handler.impl.album;

import faang.school.achievement.event.album.AlbumCreatedEvent;
import faang.school.achievement.event.like.LikePostEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.listener.impl.album.AlbumEventListener;
import faang.school.achievement.service.achievement.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class LibrarianAchievementHandler extends AbstractEventHandler<AlbumCreatedEvent> {

    private final String ACHIEVEMENT_TITLE = "LIBRARIAN";

    public LibrarianAchievementHandler(AchievementCache achievementCache,
                                       AchievementService achievementService) {
        super(achievementCache, achievementService);
    }

    @Override
    public boolean canHandleEventType(Class<?> eventType) {
        return AlbumEventListener.class.equals(eventType);
    }

    @Override
    public void handleEvent(AlbumCreatedEvent event) {
        handleAchievement(event.getUserId(), ACHIEVEMENT_TITLE);
    }
}