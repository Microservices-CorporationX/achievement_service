package faang.school.achievement.handler.album;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.album.AlbumCreatedEvent;
import faang.school.achievement.exception.DataValidationException;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LibrarianAchievementHandler implements EventHandler<AlbumCreatedEvent> {

    private final AchievementCache achievementCache;
    private final AchievementService achievementService;
    @Value("${spring.data.achievement.librarian}")
    private String achievementKey;

    @Override
    public boolean handleEvent(AlbumCreatedEvent event) {
        log.info("Handling event {}", event);
        AchievementDto achievementDto = achievementCache.get(achievementKey);

        if (achievementDto == null) {
            log.error("Achievement with key {} not found", achievementKey);
            throw new DataValidationException("Achievement with key " + achievementKey + "  not found");
        }

        long userId = event.getUserId();
        long achievementId = achievementDto.getId();

        if (achievementService.hasAchievement(userId, achievementId)){
            log.info("The user {} already has an achievement with id {}"
                    , userId, achievementId);
            return false;
        }

        achievementService.createProgressIfNecessary(userId, achievementId);
        AchievementProgress progress = achievementService.getProgress(userId, achievementId);
        long currentPoints = progress.getCurrentPoints() + 1;
        progress.setCurrentPoints(currentPoints);
        achievementService.saveProgress(progress);

        if (currentPoints >= achievementDto.getPoints()) {
            achievementService.giveAchievement(achievementDto, userId);
            log.info("User {} has now received the {} achievement.", userId, achievementKey);
            return true;
        }

        log.info("Finished handleEvent for user {} ", userId);
        return false;
    }
}
