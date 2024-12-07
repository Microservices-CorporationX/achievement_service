package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementType;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractEventHandler<T> implements EventHandler<T> {
    private final AchievementService achievementService;

    protected List<Achievement> getAchievementsByType(AchievementType type) {
        return achievementService.findByType(type);
    }

    protected boolean processAchievement(long userId, Achievement achievement) {
        boolean userHasAchievement = false;

        if (achievementService.isUserHasAchievement(userId, achievement.getId())) {
            userHasAchievement = true;
        } else {
            var result = achievementService.processAchievementProgress(userId, achievement);
            System.out.println(result);
        }

        return userHasAchievement;
    }
}
