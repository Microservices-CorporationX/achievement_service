package faang.school.achievement.handler;

import faang.school.achievement.event.AlbumCreatedEvent;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementProgressService;
import faang.school.achievement.service.AchievementService;
import org.springframework.stereotype.Service;

@Service
public class LibrarianAchievementHandler extends AbstractAchievementHandler {

    public LibrarianAchievementHandler(AchievementCache achievementCache,
                                       AchievementService achievementService,
                                       AchievementProgressService achievementProgressService) {
        super(achievementCache, achievementService, achievementProgressService);
    }

    @Override
    public void handleEvent(AlbumCreatedEvent event) {
        handleAchievement(event, "LIBRARIAN");
    }
}