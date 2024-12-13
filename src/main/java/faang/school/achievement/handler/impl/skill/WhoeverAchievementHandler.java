package faang.school.achievement.handler.impl.skill;

import faang.school.achievement.event.skill.SkillAcquiredEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.service.achievement.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import org.springframework.stereotype.Component;

@Component
public class WhoeverAchievementHandler extends AbstractEventHandler<SkillAcquiredEvent> {
    private static final String ACHIEVEMENT_TITLE = "SKILL_MASTER";

    public WhoeverAchievementHandler(
            AchievementCache achievementCache,
            AchievementService achievementService
    ) {
        super(achievementCache, achievementService);
    }

    @Override
    public void handleEvent(SkillAcquiredEvent event) {
        handleAchievement(event.getRecipientId(), ACHIEVEMENT_TITLE);
    }
}
