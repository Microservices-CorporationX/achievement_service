package faang.school.achievement.service;

import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementProgressService {
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementService userAchievementService;

    public void createProgressIfNecessary(long userId, long achievementId) {
        validateUserId(userId);
        validateAchievementId(achievementId);
        if (!hasAchievement(userId, achievementId)) {
            achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
        }
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        validateUserId(userId);
        validateAchievementId(achievementId);
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId).orElseThrow(() -> {
            log.error("No AchievementProgress found [userId={}, achievementId={}]", userId, achievementId);
            return new EntityNotFoundException(String.format(
                    "No achievement progress found [userId=%d, achievementId=%d]", userId, achievementId
            ));
        });
    }

    private boolean hasAchievement(long userId, long achievementId) {
        validateUserId(userId);
        validateAchievementId(achievementId);
        return userAchievementService.hasAchievement(userId, achievementId);
    }

    private void validateUserId(long userId) {
        if (userId <= 0) {
            log.error("Exception occurred during userId validation, caused by:" +
                    " userId can not be less or equals to 0");
            throw new IllegalArgumentException("Invalid userId");
        }
    }

    private void validateAchievementId(long achievementId) {
        if (achievementId <= 0) {
            log.error("Exception occurred during achievementId validation, caused by:" +
                    " achievementId can not be less or equals to 0");
            throw new IllegalArgumentException("Invalid userId");
        }
    }
}
