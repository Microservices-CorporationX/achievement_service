package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;

    public boolean hasAchievement(Long userId, Long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void createProgressIfNecessary(Long userId, Long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public AchievementProgress getProgress(Long userId, Long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new IllegalStateException("Progress not found"));
    }

    public void giveAchievement(Long userId, Achievement achievement) {
        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievement)
                .userId(userId)
                .build();
        userAchievementRepository.save(userAchievement);
    }
}