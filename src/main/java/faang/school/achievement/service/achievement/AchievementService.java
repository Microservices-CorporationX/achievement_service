package faang.school.achievement.service.achievement;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.validator.achievement.AchievementServiceValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementServiceValidator validator;

    public boolean hasAchievement(long userId, long achievementId) {
        log.info("validate Argument");
        validator.checkId(userId, achievementId);

        log.info("check whether the user has an achievement");
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    public Achievement getAchievementByTitle(String title) {
        log.info("validate Argument");
        validator.checkTitle(title);

        log.info("getting achievement from db by id");
        return achievementRepository.getAchievementByTitle(title);
    }

    public void createProgressIfNecessary(long userId, long achievementId) {
        log.info("validate Argument");
        validator.checkId(userId, achievementId);

        log.info("create new achievementProgress with given userId and achievementId");
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        log.info("validate Argument");
        validator.checkId(userId, achievementId);

        log.info("create new achievementProgress with given userId and achievementId if needed");
        createProgressIfNecessary(userId, achievementId);

        log.info("getting achievementProgress from db by userId and achievementId");
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Entity was not found at db"));
    }

    public void createNewUserAchievement(UserAchievement userAchievement) {
        log.info("validate Argument");
        validator.checkUserAchievement(userAchievement);

        log.info("add new userAchievement in db");
        userAchievementRepository.save(userAchievement);
    }
}
