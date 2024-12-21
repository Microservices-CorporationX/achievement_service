package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementCacheDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.event.AchievementEvent;
import faang.school.achievement.exception.AchievementAlreadyExistsException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.AchievementEventPublisher;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;
    private final AchievementEventPublisher achievementEventPublisher;
    private final UserAchievementRepository userAchievementRepository;

    @Transactional
    @Cacheable(value = "achievements", key = "#title.toUpperCase()")
    public AchievementCacheDto getAchievementByTitle(String title) {
        Achievement achievement = achievementRepository.findByTitle(title.toUpperCase()).orElseThrow(() ->
                new EntityNotFoundException("Achievement not found"));
        log.info("Get achievement by name from Database: {}", title);
        return achievementMapper.toDto(achievement);
    }
}
    public void publishAchievementEvent(long userId, long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Achievement %d not found", achievementId)));

        createUserAchievement(userId, achievementId, achievement);

        AchievementEvent achievementEvent = AchievementEvent.builder()
                .achievementId(achievementId)
                .userId(userId)
                .build();

        achievementEventPublisher.publish(achievementEvent);
    }

    private void createUserAchievement(long userId, long achievementId, Achievement achievement) {
        try {
            UserAchievement userAchievement = UserAchievement.builder()
                    .userId(userId)
                    .achievement(achievement)
                    .build();

            userAchievementRepository.save(userAchievement);

        } catch (DataIntegrityViolationException ex) {
            throw new AchievementAlreadyExistsException(String.format(
                    "User %d already has achievement %d", userId, achievementId));
        }
    }
}
