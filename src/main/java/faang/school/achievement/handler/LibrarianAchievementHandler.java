package faang.school.achievement.handler;

import faang.school.achievement.model.AlbumCreatedEvent;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementProgressService;
import faang.school.achievement.service.AchievementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LibrarianAchievementHandler extends AbstractAchievementHandler implements EventHandler {

    public LibrarianAchievementHandler(AchievementCache achievementCache,
                                       AchievementService achievementService,
                                       AchievementProgressService achievementProgressService) {
        super(achievementCache, achievementService, achievementProgressService);
    }

    @Override
    public void handleEvent(Long userId) {
        handleAchievement(userId, "LIBRARIAN");
    }

    @Override
    public Class<?> requiredEvent() {
        return AlbumCreatedEvent.class;
    }
}
