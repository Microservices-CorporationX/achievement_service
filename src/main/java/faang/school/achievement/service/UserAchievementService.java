package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAchievementService {
    private final UserAchievementRepository userAchievementRepository;

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    public void giveAchievement(Long userId, Achievement achievement) {
        userAchievementRepository.save(create(userId, achievement));
    }

    private UserAchievement create(Long userId, Achievement achievement) {
        return UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
