package faang.school.achievement.service;

import faang.school.achievement.exception.AchievementAlreadyExistsException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAchievementService {
    private final UserAchievementRepository userAchievementRepository;

    public void createUserAchievement(long userId, long achievementId, Achievement achievement) {
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
