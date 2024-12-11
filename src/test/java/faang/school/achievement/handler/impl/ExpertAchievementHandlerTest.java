package faang.school.achievement.handler.impl;

import faang.school.achievement.event.CommentEventDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpertAchievementHandlerTest {
    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementCache achievementCache;

    private EventHandlerForTest eventHandlerForTest;

    @BeforeEach
    void setUp() {
        eventHandlerForTest = new EventHandlerForTest(achievementService, achievementCache);
    }

    @Test
    public void handleEventHasAchievementTest() {
        long userId = 1L;
        long achievementId = 2L;
        String achievementName = "someAchievement";

        Achievement achievement = new Achievement();
        achievement.setId(achievementId);

        CommentEventDto eventDto = new CommentEventDto();
        eventDto.setCommenterId(userId);

        when(achievementCache.getAchievement(achievementName)).thenReturn(achievement);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(true);

        eventHandlerForTest.handleEvent(eventDto);

        verify(achievementCache).getAchievement(achievementName);
        verify(achievementService).hasAchievement(userId, achievementId);
        verify(achievementService, times(0)).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    public void handleEventProgressNotEnoughTest() {
        long userId = 1L;
        long achievementId = 2L;
        String achievementName = "someAchievement";

        Achievement achievement = new Achievement();
        achievement.setId(achievementId);

        CommentEventDto eventDto = new CommentEventDto();
        eventDto.setCommenterId(userId);

        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(1);

        when(achievementCache.getAchievement(achievementName)).thenReturn(achievement);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(achievementProgress);

        eventHandlerForTest.handleEvent(eventDto);

        verify(achievementCache).getAchievement(achievementName);
        verify(achievementService).hasAchievement(userId, achievementId);
        verify(achievementService).createProgressIfNecessary(userId, achievementId);
        verify(achievementService).getProgress(userId, achievementId);
        verify(achievementService).saveProgress(achievementProgress);
    }

    @Test
    public void handleEventTest() {
        long userId = 1L;
        long achievementId = 2L;
        String achievementName = "someAchievement";

        Achievement achievement = new Achievement();
        achievement.setId(achievementId);
        achievement.setPoints(3);

        CommentEventDto commentEventDto = new CommentEventDto();
        commentEventDto.setCommenterId(userId);

        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(2);

        AchievementProgress addedProgress = new AchievementProgress();
        addedProgress.setCurrentPoints(2);
        addedProgress.increment();

        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setAchievement(achievement);
        userAchievement.setUserId(userId);

        when(achievementCache.getAchievement(achievementName)).thenReturn(achievement);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(achievementProgress);

        eventHandlerForTest.handleEvent(commentEventDto);

        verify(achievementCache).getAchievement(achievementName);
        verify(achievementService).hasAchievement(userId, achievementId);
        verify(achievementService).createProgressIfNecessary(userId, achievementId);
        verify(achievementService).getProgress(userId, achievementId);
        verify(achievementService).saveProgress(addedProgress);
        verify(achievementService).giveAchievement(userAchievement);
    }
}
