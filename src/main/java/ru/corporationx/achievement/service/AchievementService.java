package ru.corporationx.achievement.service;

import ru.corporationx.achievement.dto.AchievementDto;
import ru.corporationx.achievement.exception.DataValidationException;
import ru.corporationx.achievement.mapper.AchievementMapper;
import ru.corporationx.achievement.model.AchievementProgress;
import ru.corporationx.achievement.repository.AchievementProgressRepository;
import ru.corporationx.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementService {

    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementMapper achievementMapper;

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
        log.debug("Progress towards achievement with ID {} for user with ID {} has been created!",
                achievementId, userId);
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new DataValidationException(
                        String.format("Achievement progress not found for userId: %d and achievementId: %d", userId, achievementId)));
    }

    public void saveProgress(AchievementProgress progress) {
        achievementProgressRepository.save(progress);
    }

    public void giveAchievement(AchievementDto achievementDto, long userId) {
        userAchievementRepository.giveAchievement(userId, achievementDto.getId());
        log.info("User with ID {} received a new achievement.", userId);
    }
}
