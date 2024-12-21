package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class HandsomeAchievementHandler implements EventHandler<ProfilePicEvent> {

    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    @Async
    @Override
    public void handleEvent(ProfilePicEvent event) {
        log.info("Processing event for user {}", event.getUserId());

        Optional<Achievement> achievementOpt = achievementCache.getAchievement("Handsome");

        if (achievementOpt.isEmpty()) {
            log.warn("Achievement 'Handsome' not found");
            return;
        }

        Achievement achievement = achievementOpt.get();

        if (achievementService.hasAchievement(event.getUserId(), achievement.getId())) {
            log.info("User {} already has achievement 'Handsome'", event.getUserId());
            return;
        }

        achievementService.createProgressIfNecessary(event.getUserId(), achievement.getId());
        AchievementProgress progress = achievementService.getProgress(event.getUserId(), achievement.getId());

        progress.increment();
        if (progress.getCurrentPoints() >= 10) {
            achievementService.giveAchievement(event.getUserId(), achievement);
            log.info("Achievement 'Handsome' awarded to user {}", event.getUserId());
        }
    }
}