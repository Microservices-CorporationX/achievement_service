package faang.school.achievement.validator.achievement;

import faang.school.achievement.model.UserAchievement;
import org.springframework.stereotype.Component;

@Component
public class AchievementServiceValidator {
    public void checkId(long... ids) {
        for (long i : ids) {
            if (i < 0) {
                throw new IllegalArgumentException("Id не может быть отрицательным");
            }
        }
    }

    public void checkTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title не может быть пустым");
        }
    }

    public void checkUserAchievement(UserAchievement userAchievement) {
        if (userAchievement == null ||
                userAchievement.getAchievement() == null ||
                userAchievement.getUserId() < 0 ||
                userAchievement.getCreatedAt() == null ||
                userAchievement.getUpdatedAt() == null) {
            throw new IllegalArgumentException("illegal userAchievement to save at db");
        }
    }
}
