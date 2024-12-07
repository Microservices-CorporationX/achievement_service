package faang.school.achievement.service;

import faang.school.achievement.annotations.aop.AchievementAcceptedAspect;
import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.ResourceNotFoundException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.AchievementType;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementCache achievementCache;
    private final AchievementMapper achievementMapper;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;

    @Transactional(readOnly = true)
    public AchievementDto getByTitle(String title) {
        AchievementDto fromCache = achievementCache.getFromCache(title);
        if (fromCache != null) {
            return fromCache;
        }

        Achievement fromDb = findByTitle(title);
        achievementCache.addToCache(fromDb);

        return achievementMapper.toDto(fromDb);
    }

    @Transactional(readOnly = true)
    public Achievement findByTitle(String title) {
        return achievementRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement", title));
    }

    @Transactional(readOnly = true)
    public List<Achievement> findByType(AchievementType type) {
        return achievementRepository.findByType(type);
    }

    @Transactional(readOnly = true)
    public boolean isUserHasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @AchievementAcceptedAspect
    @Transactional
    public UserAchievement processAchievementProgress(long userId, Achievement achievement) {
        long achievementId = achievement.getId();
        long baseValue = achievement.getBaseValue();
        AchievementProgress achievementProgress = getOrCreateProgress(userId, achievementId, baseValue);

        incrementProgress(achievementProgress);
        if (achievementProgress.getCurrentPoints() >= achievement.getThreshold()) {
            var result = assignAchievementToUser(achievementProgress);
            return result;
        } else {
            return new UserAchievement();
        }
    }

    @Transactional
    public AchievementProgress getOrCreateProgress(long userId, long achievementId, long baseValue) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId, baseValue);
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new ResourceNotFoundException("AchievementProgress", achievementId));
    }

    @Transactional
    public void incrementProgress(AchievementProgress achievementProgress) {
        achievementProgress.increment();
        achievementProgressRepository.save(achievementProgress);
    }

    @Transactional(readOnly = true)
    public List<AchievementProgress> getAllNotCompletedProgresses() {
        return achievementProgressRepository.findAllByCompleted(false);
    }


    @Transactional
    public UserAchievement assignAchievementToUser(AchievementProgress achievementProgress) {
        achievementProgress.setCompleted(true);
        achievementProgressRepository.save(achievementProgress);

        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievementProgress.getAchievement())
                .userId(achievementProgress.getUserId())
                .build();
        UserAchievement createdUserAchievement = userAchievementRepository.save(userAchievement);
        return createdUserAchievement;
    }
}
