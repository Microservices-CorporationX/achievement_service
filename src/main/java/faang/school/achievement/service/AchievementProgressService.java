package faang.school.achievement.service;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementProgressService {
    private final AchievementProgressRepository achievementProgressRepository;

    @Transactional
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Transactional
    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Achievement progress for user id - " + userId +
                                " and achievement id - " + achievementId + " - not found"));
    }

    @Transactional
    public AchievementProgress saveProgress(AchievementProgress achievementProgress) {
        log.info("User {} have point {} of {} for achievement {}",
                achievementProgress.getUserId(),
                achievementProgress.getCurrentPoints(),
                achievementProgress.getAchievement().getPoints(),
                achievementProgress.getAchievement().getTitle()
        );
        return achievementProgressRepository.save(achievementProgress);
    }
}
