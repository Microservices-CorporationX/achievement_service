package faang.school.achievement.service.achievement;

import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;

    public boolean hasAchievement(Long userId, Long achievementId) {
        boolean result = userAchievementRepository.
                existsByUserIdAndAchievementId(userId, achievementId);
        log.info("hasAchievement result: {}, for user with Id: {}", result, userId);
        return result;
    }

    @Transactional
    public void createProgressIfNecessary(Long userId, Long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public AchievementProgress getProgress(Long userId, Long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Achievement with Id: %d not found, for user with Id: %d",
                        achievementId, userId)));
    }

    public void saveProgress(AchievementProgress progress) {
        achievementProgressRepository.save(progress);
        log.info("Save AchievementProgress with Id: {}, for user with Id: {}",
                progress.getId(), progress.getUserId());
    }

    public void giveAchievement(UserAchievement userAchievement) {
        userAchievementRepository.save(userAchievement);
        log.info("Achievement: {} gave successfully to user with Id: {}",
                userAchievement.getAchievement().getTitle(), userAchievement.getUserId());
    }
}
