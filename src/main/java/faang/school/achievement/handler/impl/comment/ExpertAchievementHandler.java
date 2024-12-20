package faang.school.achievement.handler.impl.comment;

import faang.school.achievement.event.comment.CommentEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExpertAchievementHandler extends AbstractEventHandler<CommentEvent> {

    private final static String ACHIEVEMENT_TITLE = "EXPERT";

    public ExpertAchievementHandler(AchievementCache achievementCache,
                                           AchievementService achievementService) {
        super(achievementCache, achievementService);
    }

    @Override
    public void handleEvent(CommentEvent event) {
        handleAchievement(event.getCommenterId(), ACHIEVEMENT_TITLE);
    }

    @Override
    public boolean supportsEvent(Class<?> eventType) {
        return CommentEvent.class.equals(eventType);
    }
}
