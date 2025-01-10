package faang.school.achievement.handler.post.writer;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.mentorship.MentorshipStartEvent;
import faang.school.achievement.dto.post.PostEvent;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.handler.post.PostEventHandler;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WriterAchievementHandler extends PostEventHandler {
    @Value("${achievements.post.writer}")
    private String writerAchievementKey;
    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    @Override
    public void handleEvent(PostEvent event) {
        log.info("Starting handle event for user with ID {}", event.userId());

        AchievementDto achievementDto = getAndValidateAchievement(writerAchievementKey);

        if(achievementService.hasAchievement(event.userId(), achievementDto.getId())) {
            log.debug("The user with ID {} already has the {} achievement.", event.userId(), achievementDto.getTitle());
            return;
        }

        achievementService.createProgressIfNecessary(event.userId(), achievementDto.getId());
        AchievementProgress achievementProgress = achievementService.getProgress(event.userId(), achievementDto.getId());
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

    private void hasComplete(AchievementDto achievementDto, AchievementProgress achievementProgress, PostEvent event) {
        if (achievementDto.getPoints() == achievementProgress.getCurrentPoints()) {
            achievementService.giveAchievement(achievementDto, event.userId());
            log.info("User with ID {} has now received the {} achievement.", event.userId(), achievementDto.getTitle());
        }
        log.info("Finished handle event for user with ID {}", event.userId());
    }
}
