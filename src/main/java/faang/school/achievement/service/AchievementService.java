package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.event.AchievementEvent;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.AchievementPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementService {
    private final Cache<Achievement> achievementCache;
    private final AchievementMapper achievementMapper;
    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementPublisher achievementPublisher;

    public AchievementDto get(String title) {
        log.info("Requested achievement with title " + title);
        return achievementMapper.toDto(achievementCache.get(title));
    }

    public List<AchievementDto> getAll() {
        log.info("Requested all achievements");
        return achievementMapper.toDtoList(achievementCache.getAll());
    }

    @Transactional(readOnly = true)
    public boolean hasAchievement(Long userId, Long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void createProgressIfNecessary(Long userId, Long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Transactional(readOnly = true)
    public AchievementProgress getProgress(Long userId, Long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException("AchievementProgress not found"));
    }

    @Transactional
    public void updateProgress(AchievementProgress achievementProgress) {
        achievementProgressRepository.save(achievementProgress);
        log.info("Progress with id: {} updated successfully", achievementProgress.getId());
    }

    @Transactional
    public void giveAchievement(Long userId, Long achievementId) {
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(getAchievementById(achievementId))
                .build();
        userAchievementRepository.save(userAchievement);
        log.info("User with id: {} received achievement with id: {}", userId, achievementId);
        AchievementEvent achievementEvent = AchievementEvent.builder()
                .achievementId(achievementId)
                .userId(userId)
                .build();
        achievementPublisher.publish(achievementEvent);
    }

    private Achievement getAchievementById(Long achievementId) {
        return achievementRepository.findById(achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Achievement not found"));
    }
}
