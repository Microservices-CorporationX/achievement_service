package faang.school.achievement.handler.impl.comment;

import faang.school.achievement.event.comment.CommentEvent;
import faang.school.achievement.handler.AbstractEventHandler;

import faang.school.achievement.service.achievement.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import org.springframework.stereotype.Component;

@Component
public class EvilCommenterAchievementHandler extends AbstractEventHandler<CommentEvent> {

    private final static String EVIL_COMMENTER_TITLE = "EVIL COMMENTER";

    public EvilCommenterAchievementHandler(AchievementService achievementService,
                                           AchievementCache achievementCache) {
        super(achievementCache, achievementService);
    }

    @Override
    public void handleEvent(CommentEvent event) {
        handleAchievement(event.getCommenterId(), EVIL_COMMENTER_TITLE);
    }
}