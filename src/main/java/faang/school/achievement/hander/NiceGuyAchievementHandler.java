package faang.school.achievement.hander;

import faang.school.achievement.dto.AchievementCacheDto;
import faang.school.achievement.enums.AchievementTitle;
import faang.school.achievement.event.RecommendationEvent;
import faang.school.achievement.exception.HandleEventProcessingException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementProgressService;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.UserAchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class NiceGuyAchievementHandler extends RecommendationEventHandler {
    private final AchievementService achievementService;
    private final UserAchievementService userAchievementService;
    private final AchievementProgressService achievementProgressService;
    private final AchievementMapper achievementMapper;

    @Override
    @Async
    @Transactional
    public void handleEvent(RecommendationEvent event) {
        try {
            AchievementCacheDto achievementCacheDto = achievementService.getAchievementByTitle(AchievementTitle.NICE_GUY.getValue());
            achievementProgressService.createProgressIfNecessary(event.receivedId(), achievementCacheDto.getId());
            AchievementProgress progress = achievementProgressService.getProgress(event.receivedId(),
                    achievementCacheDto.getId());
            progress.increment();
            if (progress.getCurrentPoints() == achievementCacheDto.getPoints()) {
                Achievement achievement = achievementMapper.toEntity(achievementCacheDto);
                userAchievementService.giveAchievement(event.receivedId(), achievement);
            }
        } catch (Exception e) {
            String errorMessage = String.format(
                    "Error processing recommendation event. User ID: %s, Achievement: NICE GUY, Error: %s",
                    event.receivedId(), e.getMessage());
            log.error(errorMessage, e);
            throw new HandleEventProcessingException(errorMessage, e);
        }
    }
}

