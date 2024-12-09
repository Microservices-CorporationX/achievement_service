package faang.school.achievement.handler.impl;

import faang.school.achievement.event.SkillAcquiredEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.handler.AchievementProgressHandler;
import faang.school.achievement.service.AchievementCache;
import org.springframework.stereotype.Component;

@Component
public class WhoeverAchievementHandler extends AbstractEventHandler<SkillAcquiredEvent> {
    private static final String ACHIEVEMENT_TITLE = "SKILL_MASTER";

    public WhoeverAchievementHandler(
            AchievementCache achievementCache,
            AchievementProgressHandler achievementProgressHandler
    ) {
        super(achievementCache, achievementProgressHandler);
    }

    @Override
    public void handle(SkillAcquiredEvent event) {
        handleAchievement(event.getRecipientId(), ACHIEVEMENT_TITLE);
    }
}
