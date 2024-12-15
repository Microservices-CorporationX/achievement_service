package faang.school.achievement.service;

import faang.school.achievement.mapper.achievement.AchievementMapper;
import faang.school.achievement.dto.achievement.AchievementDto;
import faang.school.achievement.dto.achievement.AchievementProgressDto;
import faang.school.achievement.mapper.achievement.AchievementProgressMapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConglomerateAchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementMapper achievementMapper;
    private final AchievementProgressMapper achievementProgressMapper;

    @Value("${application.constants.increment_progress_points}")
    private int incrementProgressAmount;

    public AchievementDto getAchievement(String title){
        Achievement achievement = achievementRepository.findByTitle(title).orElseThrow(
                () -> new IllegalArgumentException("No achievement found for title: " + title)
        );

        return achievementMapper.toDto(achievement);
    }

    public boolean hasAchievement(Long userId, Long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void createProgressIfNecessary(Long userId, Long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public AchievementProgressDto getProgress(Long userId, Long achievementId) {
        AchievementProgress achievementProgress = achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId).orElseThrow(
                () -> new EntityNotFoundException("Progress not found for achievement id: " + achievementId)
        );

        return achievementProgressMapper.toDto(achievementProgress);
    }

    public void incrementProgress(Long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId).orElseThrow(
                () -> new IllegalArgumentException("No achievement found for title")
        );

        achievement.setPoints(achievement.getPoints() + incrementProgressAmount);
    }

    public void giveAchievement(Long userId, String achievementTitle) {
        Achievement achievement = achievementRepository.findByTitle(achievementTitle).orElseThrow(
                () -> new IllegalArgumentException("No achievement found for title: " + achievementTitle)
        );

        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievement)
                .userId(userId)
                .build();

        userAchievementRepository.save(userAchievement);
    }
}
