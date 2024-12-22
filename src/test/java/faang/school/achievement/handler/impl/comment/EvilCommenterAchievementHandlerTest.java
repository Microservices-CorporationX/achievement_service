package faang.school.achievement.handler.impl.comment;

import faang.school.achievement.event.comment.CommentEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EvilCommenterAchievementHandlerTest {

    private static final String ACHIEVEMENT_TITLE = "EVIL COMMENTER";

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementCache achievementCache;

    @InjectMocks
    private EvilCommenterAchievementHandler eventHandler;

    @Test
    public void handleEventHasAchievementTest() {
        long userId = 1L;
        long achievementId = 2L;

        Achievement achievement = new Achievement();
        achievement.setId(achievementId);

        CommentEvent eventDto = new CommentEvent();
        eventDto.setCommenterId(userId);

        when(achievementCache.get(ACHIEVEMENT_TITLE)).thenReturn(achievement);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(true);

        eventHandler.handleEvent(eventDto);

        verify(achievementCache).get(ACHIEVEMENT_TITLE);
        verify(achievementService).hasAchievement(userId, achievementId);
        verify(achievementService, times(0)).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    public void handleEventProgressNotEnoughTest() {
        long userId = 1L;
        long achievementId = 2L;

        Achievement achievement = new Achievement();
        achievement.setId(achievementId);

        CommentEvent eventDto = new CommentEvent();
        eventDto.setCommenterId(userId);

        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(1);

        when(achievementCache.get(ACHIEVEMENT_TITLE)).thenReturn(achievement);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(achievementProgress);

        eventHandler.handleEvent(eventDto);

        verify(achievementCache).get(ACHIEVEMENT_TITLE);
        verify(achievementService).hasAchievement(userId, achievementId);
        verify(achievementService).createProgressIfNecessary(userId, achievementId);
        verify(achievementService).getProgress(userId, achievementId);
    }

    @Test
    public void handleEventTest() {
        long userId = 1L;
        long achievementId = 2L;

        Achievement achievement = new Achievement();
        achievement.setId(achievementId);
        achievement.setPoints(3);

        CommentEvent commentEventDto = new CommentEvent();
        commentEventDto.setCommenterId(userId);

        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(2);

        AchievementProgress addedProgress = new AchievementProgress();
        addedProgress.setCurrentPoints(2);
        addedProgress.increment();

        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setAchievement(achievement);
        userAchievement.setUserId(userId);

        when(achievementCache.get(ACHIEVEMENT_TITLE)).thenReturn(achievement);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(achievementProgress);

        eventHandler.handleEvent(commentEventDto);

        verify(achievementCache).get(ACHIEVEMENT_TITLE);
        verify(achievementService).hasAchievement(userId, achievementId);
        verify(achievementService).createProgressIfNecessary(userId, achievementId);
        verify(achievementService).getProgress(userId, achievementId);
        verify(achievementService).giveAchievement(userId, achievementId);
    }
}
