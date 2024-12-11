package faang.school.achievement.handler.impl;

import faang.school.achievement.event.CommentEventDto;
import faang.school.achievement.handler.AbstractCommentEventHandler;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;

public class EventHandlerForTest extends AbstractCommentEventHandler {

    public EventHandlerForTest(AchievementService achievementService, AchievementCache achievementCache) {
        super(achievementService, achievementCache);
    }

    @Override
    public void handleEvent(CommentEventDto event) {
        handleCommentEvent(event, "someAchievement");
    }

}
