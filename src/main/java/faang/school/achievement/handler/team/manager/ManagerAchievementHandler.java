package faang.school.achievement.handler.team.manager;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.DataValidationException;
import faang.school.achievement.handler.team.TeamEventHandler;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.dto.team.TeamEvent;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagerAchievementHandler extends TeamEventHandler {

    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    @Override
    public void handleEvent(TeamEvent event) {
        log.info("Starting handleEvent for authorId: {}", event.getAuthorId());
        AchievementDto achievement = achievementCache.get("MANAGER");

        if (achievement == null) {
            log.error("Achievement with key 'MANAGER' not found.");
            throw new DataValidationException("Achievement with key 'MANAGER' not found.");
        }

        if (achievementService.hasAchievement(event.getAuthorId(), achievement.getId())) {
            log.debug("The user with ID {} already has the Manager achievement.", event.getAuthorId());
            return;
        }

        achievementService.createProgressIfNecessary(event.getAuthorId(), achievement.getId());
        AchievementProgress progress = achievementService.getProgress(event.getAuthorId(), achievement.getId());
        progress.setCurrentPoints(progress.getCurrentPoints() + 1);
        achievementService.saveProgress(progress);

        if (achievement.getPoints() == progress.getCurrentPoints()) {
            log.info("User with ID {} has now received the Manager achievement.", event.getAuthorId());
            achievementService.giveAchievement(achievement, event.getAuthorId());
        }
        log.info("Finished handleEvent for authorId: {}", event.getAuthorId());
    }
}
