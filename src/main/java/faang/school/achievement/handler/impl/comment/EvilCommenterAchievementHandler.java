package faang.school.achievement.handler.impl.comment;

import faang.school.achievement.event.comment.CommentEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.springframework.stereotype.Component;

@Component
public class EvilCommenterAchievementHandler extends AbstractEventHandler<CommentEvent> {

    private final static String EVIL_COMMENTER_TITLE = "EVIL COMMENTER";

    public EvilCommenterAchievementHandler(AchievementCache achievementCache,
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