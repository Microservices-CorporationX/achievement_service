package faang.school.achievement.handler.impl;

import faang.school.achievement.event.CommentEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.handler.AchievementProgressHandler;
import faang.school.achievement.service.AchievementCache;
import org.springframework.stereotype.Component;

@Component
public class EvilCommenterAchievementHandler extends AbstractEventHandler<CommentEvent> {

    private final static String EVIL_COMMENTER_TITLE = "EVIL COMMENTER";

    public EvilCommenterAchievementHandler(
            AchievementCache achievementCache,
            AchievementProgressHandler achievementProgressHandler
    ) {
        super(achievementCache, achievementProgressHandler);
    }

    @Override
    public void handle(CommentEvent event) {
        handleAchievement(event.getCommenterId(), EVIL_COMMENTER_TITLE);
    }
}