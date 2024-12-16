package faang.school.achievement.service;

import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.achievement.AchievementDto;
import faang.school.achievement.dto.achievement.AchievementFilterDto;
import faang.school.achievement.dto.achievement.AchievementProgressDto;
import faang.school.achievement.dto.achievement.UserAchievementDto;
import faang.school.achievement.filter.achievement.AchievementFilter;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.mapper.UserAchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementService {

    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;
    private final UserAchievementMapper userAchievementMapper;
    private final AchievementProgressMapper achievementProgressMapper;
    private final List<AchievementFilter> achievementFilters;
    private final UserContext userContext;

    public boolean hasAchievement(Long userId, Long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    public void createProgressIfNecessary(Long userId, Long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public AchievementProgress getProgress(Long userId, Long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new IllegalStateException("Progress not found"));
    }

    public void giveAchievement(Long userId, Achievement achievement) {
        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievement)
                .userId(userId)
                .build();
        userAchievementRepository.save(userAchievement);
    }

    @Transactional(readOnly = true)
    public List<AchievementDto> getAchievementsByFilter(AchievementFilterDto achievementFilterDto) {
        log.info("Получение достижений по фильтру с параметрами: {}", achievementFilterDto);
        List<Achievement> achievements = achievementRepository.findAll();
        log.debug("Найдено {} достижений в репозитории", achievements.size());
        List<AchievementDto> result = achievementFilters.stream()
            .filter(achievementFilter -> achievementFilter.isApplicable(achievementFilterDto))
            .reduce(achievements.stream(), (stream, filter) -> filter.apply(stream, achievementFilterDto), Stream::concat)
            .map(achievementMapper::toDto)
            .toList();
        log.info("Получено {} достижений после применения фильтров", result.size());
        return result;
    }

    @Transactional(readOnly = true)
    public List<UserAchievementDto> getAchievementsByUserId() {
        long userId = userContext.getUserId();
        log.info("Получение достижений для пользователя с ID: {}", userId);
        List<UserAchievement> userAchievements = userAchievementRepository.findByUserId(userId);
        log.debug("Найдено {} достижений для пользователя с ID: {}", userAchievements.size(), userId);
        List<UserAchievementDto> result = userAchievements.stream()
            .map(userAchievementMapper::toDto)
            .toList();
        log.info("Получено {} достижений для пользователя с ID: {}", result.size(), userId);
        return result;
    }

    @Transactional(readOnly = true)
    public AchievementDto getAchievementById(long id) {
        log.info("Получение достижения по ID: {}", id);
        Achievement achievement = achievementRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Достижение с ID: {} не найдено", id);
                return new EntityNotFoundException(String.format("Achievement with ID: %d not found", id));
            });
        log.info("Достижение с ID: {} найдено", id);
        return achievementMapper.toDto(achievement);
    }

    @Transactional(readOnly = true)
    public List<AchievementProgressDto> getAchievementProgressByUserId() {
        long userId = userContext.getUserId();
        log.info("Получение прогресса достижений для пользователя с ID: {}", userId);
        List<AchievementProgress> achievementProgresses = achievementProgressRepository.findByUserId(userId);
        log.debug("Найдено {} прогрессов достижений для пользователя с ID: {}", achievementProgresses.size(), userId);
        List<AchievementProgressDto> result = achievementProgresses.stream()
            .map(achievementProgressMapper::toDto)
            .toList();
        log.info("Получено {} прогрессов достижений для пользователя с ID: {}", result.size(), userId);
        return result;
    }
}
