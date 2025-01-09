package faang.school.achievement.eventHandler;

import faang.school.achievement.config.SenseiAchievementConfig;
import faang.school.achievement.dto.AchievementCacheDto;
import faang.school.achievement.event.MentorshipAcceptedEvent;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SenseyAchievementHandler implements EventHandler<MentorshipAcceptedEvent> {
    private final AchievementService achievementService;
    private final SenseiAchievementConfig senseiAchievementConfig;

    @Async("taskExecutor")
    public void handle(MentorshipAcceptedEvent event) {
        AchievementCacheDto achievementCacheDto = achievementService.getAchievementByTitle(senseiAchievementConfig.getTitle());
        long userId = event.getAuthorForAchievements();
        long achievementId = achievementCacheDto.getId();
        if (!achievementService.hasAchievement(userId, achievementId)) {
            achievementService.createProgress(userId, achievementId);
            AchievementProgress achievementProgress = achievementService.getProgress(userId, achievementId);
            achievementProgress.increment();
            if (achievementProgress.getCurrentPoints() == senseiAchievementConfig.getRequiredPoints()) {
                achievementService.giveAchievement(userId, achievementId);
                log.info("Achievement '{}' granted to user with id {}", senseiAchievementConfig.getTitle(), userId);
            }
            achievementService.saveProgress(achievementProgress);
        }
    }
}
