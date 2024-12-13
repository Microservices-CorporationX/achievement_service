package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.Cache;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Transactional
    @Async("threadPool")
    public void handleAchievement(long userId, String achievementKey) {
        log.info("Trying to handle achievement {} for user {}", achievementKey, userId);
        Achievement sensei = getAchievementFromCache(achievementKey);

        if (hasAchievement(userId, sensei.getId())) {
            log.debug("User {} already has achievement {}", userId, achievementKey);
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
        log.debug("Checking whether user has already gained the achievement");
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    private void createProgressIfNecessary(long userId, long achievementId) {
        log.debug("Creating progress for achievement {} for user {}", achievementId, userId);
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    private AchievementProgress getProgress(long userId, long achievementId) {
        log.debug("Trying to get progress of achievement {} for user {}", achievementId, userId);
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> {
                    log.error("Achievement {} does not exist", achievementId);
                    return new EntityNotFoundException(String.format
                            ("Achievement %d for user %d was not found", achievementId, userId));
                });
    }

    private void handleEnoughPointsForAchievement(long userId,
                                                  AchievementProgress achievementProgress,
                                                  Achievement achievement) {
        log.debug("Checking whether user {} has earned enough points to receive achievement {}",
                userId, achievement.getTitle());

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
        log.info("Giving achievement {} to user {}", userAchievement.getAchievement(), userAchievement.getUserId());
        userAchievementRepository.save(userAchievement);
    }

    private void deleteAchievementProgress(AchievementProgress achievementProgress) {
        log.debug("Deleting achievement progress {} of user {}",
                achievementProgress.getId(), achievementProgress.getUserId());

        achievementProgressRepository.delete(achievementProgress);
    }
}
