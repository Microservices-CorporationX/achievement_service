package faang.school.achievement.service;

import faang.school.achievement.event.PublishEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.EventPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AchievementService {

    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final EventPublisher eventPublisher;


    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    public void createProgressIfNecessary(long userId, long achievementId) {
       achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        AchievementProgress achievementProgress = achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(()-> new RuntimeException("error"));
        achievementProgress.increment();
        return achievementProgress;
    }

    public void giveAchievement(long userId, Achievement achievement) {
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();
        userAchievementRepository.save(userAchievement);
        PublishEvent publishEvent = PublishEvent.builder()
                .userId(userId)
                .achievementId(achievement.getId())
                .achievementName(achievement.getTitle())
                .achievementDescription(achievement.getDescription())
                .build();
        eventPublisher.publish(publishEvent);
    }

}
