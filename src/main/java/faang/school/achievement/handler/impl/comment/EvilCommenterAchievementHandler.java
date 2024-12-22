package faang.school.achievement.handler.impl.comment;

import faang.school.achievement.event.comment.CommentEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.Cache;
import org.springframework.stereotype.Component;

@Component
public class EvilCommenterAchievementHandler extends AbstractEventHandler<CommentEvent> {

    private final static String EVIL_COMMENTER_TITLE = "EVIL COMMENTER";

    public EvilCommenterAchievementHandler(Cache<Achievement> achievementCache,
                                           AchievementService achievementService) {
        super(achievementCache, achievementService);
    }

    @Override
    public void handleEvent(CommentEvent event) {
        handleAchievement(event.getCommenterId(), EVIL_COMMENTER_TITLE);
    }

    @Override
    public boolean supportsEvent(Class<?> eventType) {
        return CommentEvent.class.equals(eventType);
    }
}