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

    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public void incrementUserAchievementProgress(long progressId) {
        achievementProgressRepository.incrementUserAchievementProgress(progressId);
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId).orElseThrow(() -> {
            log.error("No AchievementProgress found [userId={}, achievementId={}]", userId, achievementId);
            return new EntityNotFoundException(String.format(
                    "No achievement progress found [userId=%d, achievementId=%d]", userId, achievementId));
        });
    }
}
