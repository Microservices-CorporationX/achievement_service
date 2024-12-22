package faang.school.achievement.handler.impl;

import faang.school.achievement.event.SkillAcquiredEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.Cache;
import faang.school.achievement.service.AchievementService;
import org.springframework.stereotype.Component;

@Component
public class WhoeverAchievementHandler extends AbstractEventHandler<SkillAcquiredEvent> {
    private static final String ACHIEVEMENT_TITLE = "SKILL_MASTER";

    public WhoeverAchievementHandler(
            Cache<Achievement> achievementCache,
            AchievementService achievementService
    ) {
        super(achievementCache, achievementService);
    }

    @Override
    public void handleEvent(SkillAcquiredEvent event) {
        handleAchievement(event.getRecipientId(), ACHIEVEMENT_TITLE);
    }
}
