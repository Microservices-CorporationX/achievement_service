package faang.school.achievement.service;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.AchievementException;
import faang.school.achievement.exception.DataValidationException;
import faang.school.achievement.filters.achievement.AchievementFilter;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final AchievementCache cache;
    private final List<AchievementFilter> achievementFilters;
    private final AchievementMapper mapper;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;

    public List<AchievementDto> getAll(AchievementDto filters) {
        Stream<AchievementDto> achievements = cache.getCache().values().stream();

        return achievementFilters.stream()
                .filter(filter -> filter.isApplicable(filters))
                .flatMap(filter -> filter.apply(achievements, filters))
                .toList();
    }

    public List<AchievementDto> getByUserId(Long userId) {
        List<Achievement> usersAchievements = userAchievementRepository.findByUserId(userId)
                .stream()
                .map(UserAchievement::getAchievement)
                .toList();

        return mapper.toDto(usersAchievements);
    }

    public AchievementDto get(Long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> {
                    log.error("Achievement {} doesn't exist", achievementId);
                    return new AchievementException("Achievement doesn't exist");
                });

        return mapper.toDto(achievement);
    }

    public List<AchievementDto> getByUserIdUnearned(Long userId) {
        List<Achievement> usersAchievements = achievementProgressRepository.findByUserId(userId)
                .stream()
                .map(AchievementProgress::getAchievement)
                .toList();

        return mapper.toDto(usersAchievements);
    }

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

    public void giveAchievement(AchievementDto achievement, long userId) {
        userAchievementRepository.save(UserAchievement.builder()
                .achievement(mapper.toEntity(achievement))
                .userId(userId)
                .build());
        log.info("User with ID {} received a new achievement.", userId);
    }
}
