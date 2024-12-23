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
        if (!hasAchievement(userId, achievementId)) {
            achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
        }
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId).orElseThrow(() -> {
            log.error("Achievement Progress not found for user with id:{} and achievement with id:{}", userId, achievementId);
            return new EntityNotFoundException("Achievement Progress not found for user with id:" + userId +
                    " and achievement with id:" + achievementId);
        });
    }

    private boolean hasAchievement(long userId, long achievementId) {
        return userAchievementService.hasAchievement(userId, achievementId);
    }
}
