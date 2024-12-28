package faang.school.achievement.handler.mentorship;

import faang.school.achievement.event.SkillAcquiredEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.achievement.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class WhoeverAchievementHandler implements SkillAcquiredHandler {
    private final AchievementService achievementService;

    @Async
    @Override
    public void handleEvent(SkillAcquiredEvent event) {
        Achievement achievement =
                achievementService.getAchievementByTitleWithOutUserAndProgress("SKILLS");

        if (!achievementService.hasAchievement(event.getUserId(), achievement.getId())) {
            AchievementProgress achievementProgress =
                    achievementService.getProgress(event.getUserId(), achievement.getId());
            achievementProgress.increment();

            if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
                UserAchievement userAchievement = new UserAchievement();
                userAchievement.setAchievement(achievement);
                userAchievement.setUserId(event.getUserId());
                userAchievement.setCreatedAt(LocalDateTime.now());
                userAchievement.setUpdatedAt(LocalDateTime.now());

                achievementService.createNewUserAchievement(userAchievement);
            }
        }
    }
}
