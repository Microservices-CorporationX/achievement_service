package faang.school.achievement.handler.subscription.followers;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.subscription.FollowerEvent;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.handler.subscription.FollowerEventHandler;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FollowersAchievementHandler extends FollowerEventHandler {

    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    @Override
    public void handleEvent(FollowerEvent event) {
        log.info("Starting handle event for user with ID {}", event.getFolloweeId());

        AchievementDto achievementDto = getAndValidateAchievement("PIPSCHIKI");

        if (achievementService.hasAchievement(event.getFolloweeId(), achievementDto.getId())) {
            log.debug("The user with ID {} already has the {} achievement.", event.getFolloweeId(), achievementDto.getTitle());
            return;
        }

        achievementService.createProgressIfNecessary(event.getFolloweeId(), achievementDto.getId());
        AchievementProgress achievementProgress = achievementService.getProgress(event.getFolloweeId(), achievementDto.getId());
        achievementProgress.increment();
        achievementService.saveProgress(achievementProgress);

        hasComplete(achievementDto, achievementProgress, event);
    }

    private AchievementDto getAndValidateAchievement(String achievementTitle) {
        AchievementDto achievement = achievementCache.get(achievementTitle);

        if (achievement == null) {
            log.error("Failed to get {} achievement from cache.", achievementTitle);
            throw new AchievementNotFoundException("Failed to get achievement from cache.");
        }

        return achievement;
    }

    private void hasComplete(AchievementDto achievementDto, AchievementProgress achievementProgress, FollowerEvent event) {
        if (achievementDto.getPoints() == achievementProgress.getCurrentPoints()) {
            achievementService.giveAchievement(achievementDto, event.getFolloweeId());
            log.info("User with ID {} has now received the {} achievement.", event.getFolloweeId(), achievementDto.getTitle());
        }
        log.info("Finished handle event for user with ID {}", event.getFolloweeId());
    }
}
