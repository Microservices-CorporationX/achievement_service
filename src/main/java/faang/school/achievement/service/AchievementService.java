package faang.school.achievement.service;

import faang.school.achievement.exception.AchievementAlreadyExistsException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;

    @Transactional
    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }


    @Transactional
    public void giveAchievement(Long userId, Achievement achievement) {
        UserAchievement userAchievement = UserAchievement.builder()
                .id(userId)
                .achievement(achievement)
                .build();

        userAchievementRepository.save(userAchievement);
    }

    @Transactional
    public void createProgressIfNecessary(long userId, long achievementId) {
        if (!userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)) {
            AchievementProgress progress = AchievementProgress.builder()
                    .userId(userId)
                    .id(achievementId)
                    .currentPoints(0)
                    .build();

            achievementProgressRepository.save(progress);
        }
    }

    @Transactional
    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Progress not found for userId: " +
                        userId + ", achievementId: " + achievementId));
    }

    @Transactional
    public void updateProgress(AchievementProgress progress) {
        progress.increment();
        achievementProgressRepository.save(progress);
    }

    private void createUserAchievement(long userId, long achievementId, Achievement achievement) {
        try {
            UserAchievement userAchievement = UserAchievement.builder()
                    .userId(userId)
                    .achievement(achievement)
                    .build();

            userAchievementRepository.save(userAchievement);

        } catch (DataIntegrityViolationException ex) {
            throw new AchievementAlreadyExistsException(String.format(
                    "User %d already has achievement %d", userId, achievementId));
        }
    }
}
