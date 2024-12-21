package faang.school.achievement.handler.team.manager;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.AchievementNotFoundException;
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
    public boolean handleEvent(TeamEvent event) {
        log.info("Starting handleEvent for authorId: {}", event.getAuthorId());
        AchievementDto achievement = getAndValidateAchievement();

        if (achievementService.hasAchievement(event.getAuthorId(), achievement.getId())) {
            log.debug("The user with ID {} already has the Manager achievement.", event.getAuthorId());
            return false;
        }

        achievementService.createProgressIfNecessary(event.getAuthorId(), achievement.getId());
        AchievementProgress progress = achievementService.getProgress(event.getAuthorId(), achievement.getId());
        progress.setCurrentPoints(progress.getCurrentPoints() + 1);
        achievementService.saveProgress(progress);

        if (achievement.getPoints() == progress.getCurrentPoints()) {
            log.info("User with ID {} has now received the Manager achievement.", event.getAuthorId());
            achievementService.giveAchievement(achievement, event.getAuthorId());
            return true;
        }
        log.info("Finished handleEvent for authorId: {}", event.getAuthorId());
        return false;
    }

    private AchievementDto getAndValidateAchievement() {
        AchievementDto achievement = achievementCache.get("MANAGER");

        if (achievement == null) {
            log.error("Failed to get 'MANAGER' achievement from cache.");
            throw new AchievementNotFoundException("Failed to get 'MANAGER' achievement from cache.");
        }

        return achievement;
    }
}
