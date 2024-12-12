package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.Cache;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementRepository achievementRepository;
    private final Cache cache;

    @EventListener(ApplicationReadyEvent.class)
    public void putAllAchievementsToCacheOnBoot() {
        List<Achievement> achievements = achievementRepository.findAll();
        achievements.forEach(achievement -> cache.put(achievement.getTitle(), achievement));
    }

    public void handleAchievement(long userId, String achievementCacheKey) {
        Achievement sensei = getAchievementFromCache(achievementCacheKey);

        if (hasAchievement(userId, sensei.getId())) {
            return;
        }

        createProgressIfNecessary(userId, sensei.getId());
        AchievementProgress achievementProgress = getProgress(userId, sensei.getId());
        achievementProgress.increment();
        handleEnoughPointsForAchievement(userId, achievementProgress, sensei);
    }

    private Achievement getAchievementFromCache(String achievementKey) {
        return (Achievement) cache.get(achievementKey).get();
    }

    private boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    private void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    private AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Achievement %d for user %d was not found", achievementId, userId)));
    }

    private void handleEnoughPointsForAchievement(long userId,
                                                  AchievementProgress achievementProgress,
                                                  Achievement achievement) {
        if (achievementProgress.hasEarnedEnoughPointsForAchievement()) {
            UserAchievement userAchievement = UserAchievement.builder()
                    .userId(userId)
                    .achievement(achievement)
                    .build();
            giveAchievement(userAchievement);
            deleteAchievementProgress(achievementProgress);
        }
    }

    private void giveAchievement(UserAchievement userAchievement) {
        userAchievementRepository.save(userAchievement);
    }

    private void deleteAchievementProgress(AchievementProgress achievementProgress) {
        achievementProgressRepository.delete(achievementProgress);
    }
}
