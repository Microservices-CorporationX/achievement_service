package faang.school.achievement.handler.impl.like;

import faang.school.achievement.event.like.LikePostEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.service.achievement.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import org.springframework.stereotype.Component;

@Component
public class ILoveEveryoneAchievementHandler extends AbstractEventHandler<LikePostEvent> {

    private final String ACHIEVEMENT_TITLE = "I LOVE EVERYONE";

    public ILoveEveryoneAchievementHandler(AchievementCache achievementCache,
                                           AchievementService achievementService) {
        super(achievementCache, achievementService);
    }

    @Override
    public boolean canHandleEventType(Class<?> eventType) {
        return LikePostEvent.class.equals(eventType);
    }

    @Override
    public void handleEvent(LikePostEvent event) {
        handleAchievement(event.getLikedUserId(), ACHIEVEMENT_TITLE);
    }
}
