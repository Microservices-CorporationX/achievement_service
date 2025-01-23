package faang.school.achievement.validator.achievement;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementValidator {
    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    public AchievementDto getAndValidateAchievement(String achievementTitle) {
        AchievementDto achievement = achievementCache.get(achievementTitle);
        if (achievement == null) {
            log.error("Failed to get {} achievement from cache.", achievementTitle);
            throw new AchievementNotFoundException("Failed to get achievement from cache.");
        }
        return achievement;
    }

    public boolean checkHasAchievement(long userId, long achievementId) {
        return achievementService.hasAchievement(userId, achievementId);
    }

    public AchievementProgress incrementPoints(long userId, long achievementId) {
        achievementService.createProgressIfNecessary(userId, achievementId);
        AchievementProgress achievementProgress = achievementService.getProgress(userId, achievementId);
        achievementProgress.increment();
        achievementService.saveProgress(achievementProgress);
        return achievementProgress;
    }

    public void giveAchievementIfPointsReached(AchievementDto achievementDto,
                                               AchievementProgress achievementProgress, long userId) {
        if (achievementDto.getPoints() == achievementProgress.getCurrentPoints()) {
            achievementService.giveAchievement(achievementDto, userId);
            log.info("User with ID {} has now received the {} achievement.",
                    userId, achievementDto.getTitle());
        }
    }
}
