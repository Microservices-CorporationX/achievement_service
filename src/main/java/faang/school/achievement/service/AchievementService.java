package faang.school.achievement.service;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementCache achievementCache;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;

    public Achievement getAchievementByTitle(String title) {
        return achievementCache.getAchievementByTitle(title);
    }

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void createProgressIfNecessary(Long userId, Long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public AchievementProgress getProgress(Long userId, Long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId).orElseThrow(
                () -> new NoSuchElementException(String.format(
                        "Progress on achievement: %s for user: %s not found", achievementId, userId)));
    }

    public void giveAchievement(UserAchievement userAchievement) {
        try {
            userAchievementRepository.save(userAchievement);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockingFailureException("Error of optimistic locking when issuing achievements");
        }
    }

    public void updateProgress(AchievementProgress progress) {
        achievementProgressRepository.save(progress);
    }
}
