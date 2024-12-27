package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.DataValidationException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
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
