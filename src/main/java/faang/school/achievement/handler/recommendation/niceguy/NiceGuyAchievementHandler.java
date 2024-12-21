package faang.school.achievement.handler.recommendation.niceguy;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.recommendation.RecommendationEvent;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.exception.DataValidationException;
import faang.school.achievement.handler.recommendation.RecommendationEventHandler;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NiceGuyAchievementHandler extends RecommendationEventHandler {

    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    @Override
    public void handleEvent(RecommendationEvent event) {
        log.info("Starting handleEvent for receiverId: {}", event.getReceiverId());

        AchievementDto achievement = getAndValidateAchievement();

        if (achievementService.hasAchievement(event.getReceiverId(), achievement.getId())) {
            log.debug("The user with ID {} already has the 'NICE GUY' achievement.", event.getReceiverId());
            return;
        }

        achievementService.createProgressIfNecessary(event.getReceiverId(), achievement.getId());
        AchievementProgress progress = achievementService.getProgress(event.getReceiverId(), achievement.getId());
        progress.setCurrentPoints(progress.getCurrentPoints() + 1);
        achievementService.saveProgress(progress);

        if (achievement.getPoints() == progress.getCurrentPoints()) {
            log.info("User with ID {} has now received the 'NICE GUY' achievement.", event.getReceiverId());
            achievementService.giveAchievement(achievement, event.getReceiverId());
        }
        log.info("Finished handleEvent for receiverId: {}", event.getReceiverId());
    }

    private AchievementDto getAndValidateAchievement() {
        AchievementDto achievement = achievementCache.get("NICE GUY");

        if (achievement == null) {
            log.error("Failed to get 'NICE GUY' achievement from cache.");
            throw new AchievementNotFoundException("Failed to get 'NICE GUY' achievement from cache.");
        }

        return achievement;
    }
}
