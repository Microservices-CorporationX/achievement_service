package faang.school.achievement.handler.impl;

import faang.school.achievement.event.CommentEventDto;
import faang.school.achievement.handler.AbstractCommentEventHandler;
import faang.school.achievement.service.achievement.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExpertAchievementHandler extends AbstractCommentEventHandler {

    public ExpertAchievementHandler(AchievementService achievementService,
                                    AchievementCache achievementCache) {
        super(achievementService, achievementCache);
    }

    @Override
    public void handleEvent(CommentEventDto event) {
        handleCommentEvent(event, "EXPERT");
    }
}
