package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.mapper.achievement.AchievementMapper;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementMapper achievementMapper;
    private final AchievementCache achievementCache;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;

    public AchievementDto get(String title) {
        return achievementMapper.toDto(achievementCache.get(title));
    }

    public List<AchievementDto> getAll() {
        return achievementMapper.toDtoList(achievementCache.getAll());
    }

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

    @Transactional
    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void giveAchievement(UserAchievement userAchievement) {
        log.info("User {} give achievement {}",
                userAchievement.getUserId(),
                userAchievement.getAchievement().getTitle()
        );
        userAchievementRepository.save(userAchievement);
    }
}