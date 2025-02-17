package ru.corporationx.achievement.handler.comment.expert;

import ru.corporationx.achievement.cache.AchievementCache;
import ru.corporationx.achievement.dto.AchievementDto;
import ru.corporationx.achievement.dto.comment.CommentEvent;
import ru.corporationx.achievement.exception.AchievementNotFoundException;
import ru.corporationx.achievement.handler.comment.CommentEventHandler;
import ru.corporationx.achievement.model.AchievementProgress;
import ru.corporationx.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpertAchievementHandler extends CommentEventHandler {

    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    @Override
    public void handleEvent(CommentEvent event) {
        log.info("Starting handle event for user with ID {}", event.getAuthorId());

        AchievementDto achievementDto = getAndValidateAchievement("EXPERT");

        if (achievementService.hasAchievement(event.getAuthorId(), achievementDto.getId())) {
            log.debug("The user with ID {} already has the {} achievement.", event.getAuthorId(), achievementDto.getTitle());
            return;
        }

        achievementService.createProgressIfNecessary(event.getAuthorId(), achievementDto.getId());
        AchievementProgress achievementProgress = achievementService.getProgress(event.getAuthorId(), achievementDto.getId());
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

    private void hasComplete(AchievementDto achievementDto, AchievementProgress achievementProgress, CommentEvent event) {
        if (achievementDto.getPoints() == achievementProgress.getCurrentPoints()) {
            achievementService.giveAchievement(achievementDto, event.getAuthorId());
            log.info("User with ID {} has now received the {} achievement.", event.getAuthorId(), achievementDto.getTitle());
        }
        log.info("Finished handle event for user with ID {}", event.getAuthorId());
    }
}
