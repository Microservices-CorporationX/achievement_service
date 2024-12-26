package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAchievementService {
    private final UserAchievementRepository userAchievementRepository;

    public boolean hasAchievement(long userId, long achievementId) {
        validateUserId(userId);
        validateAchievementId(achievementId);
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    public void giveAchievement(long userId, Achievement achievement) {
        validateUserId(userId);
        validateAchievement(achievement);
        userAchievementRepository.save(create(userId, achievement));
    }

    private UserAchievement create(long userId, Achievement achievement) {
        return UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
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

    private void validateAchievement(Achievement achievement) {
        if (achievement == null) {
            log.error("Exception occurred during achievement validation in UserAchievementService, " +
                    "caused by: Achievement == null");
            throw new IllegalArgumentException("Achievement can not be null");
        }
    }
}
