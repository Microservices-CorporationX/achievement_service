package faang.school.achievement.handler;

import faang.school.achievement.event.CommentEventDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCommentEventHandler implements EventHandler<CommentEventDto> {
    private final AchievementService achievementService;
    private final AchievementCache achievementCache;

    @Async("commentHandlingExecutor")
    public void handleCommentEvent(CommentEventDto event, String titleAchievement) {
        Achievement achievement = achievementCache.getAchievement(titleAchievement);
        if (!achievementService.hasAchievement(event.getCommenterId(), achievement.getId())) {
            achievementService.createProgressIfNecessary(event.getCommenterId(), achievement.getId());
            AchievementProgress achievementProgress = achievementService.getProgress(event.getCommenterId(), achievement.getId());
            achievementProgress.increment();
            achievementService.saveProgress(achievementProgress);
            if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
                UserAchievement userAchievement = new UserAchievement();
                userAchievement.setUserId(event.getCommenterId());
                userAchievement.setAchievement(achievement);
                achievementService.giveAchievement(userAchievement);
            }
        }
    }
}
