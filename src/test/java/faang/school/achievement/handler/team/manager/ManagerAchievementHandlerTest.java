package faang.school.achievement.handler.team.manager;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.exception.DataValidationException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.event.team.TeamEvent;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ManagerAchievementHandlerTest {

    @Mock
    private AchievementCache achievementCache;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private ManagerAchievementHandler managerAchievementHandler;

    @Test
    public void testHandleEventIfAchievementNull() {
        TeamEvent event = prepareEvent();
        when(achievementCache.get(anyString())).thenReturn(null);

        DataValidationException exception = assertThrows(DataValidationException.class,
                () -> managerAchievementHandler.handleEvent(event));

        assertEquals("Achievement with key 'MANAGER' not found.", exception.getMessage());
    }

    @Test
    public void testHandleEventIfUserAlreadyHasAchievement() {
        TeamEvent event = prepareEvent();
        when(achievementCache.get(anyString())).thenReturn(new Achievement());
        when(achievementService.hasAchievement(event.getAuthorId(), 0L)).thenReturn(true);

        boolean result = managerAchievementHandler.handleEvent(event);

        assertFalse(result);
    }

    @Test
    public void testHandleEventIfUserGivenAchievement() {
        TeamEvent event = prepareEvent();
        Achievement achievement = Achievement.builder()
                .id(1L)
                .points(10L)
                .build();
        AchievementProgress achievementProgress = AchievementProgress.builder()
                .id(2L)
                .currentPoints(9L)
                .build();
        when(achievementCache.get(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getAuthorId(), 1L)).thenReturn(false);
        when(achievementService.getProgress(event.getAuthorId(), achievement.getId())).thenReturn(achievementProgress);

        boolean result = managerAchievementHandler.handleEvent(event);

        verify(achievementService).createProgressIfNecessary(event.getAuthorId(), achievement.getId());
        verify(achievementService).saveProgress(achievementProgress);
        verify(achievementService).giveAchievement(achievement, event.getAuthorId());
        assertTrue(result);
    }

    @Test
    public void testHandleEventIfUserNotGivenAchievement() {
        TeamEvent event = prepareEvent();
        Achievement achievement = Achievement.builder()
                .id(1L)
                .points(10L)
                .build();
        AchievementProgress achievementProgress = AchievementProgress.builder()
                .id(2L)
                .currentPoints(8L)
                .build();
        when(achievementCache.get(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getAuthorId(), 1L)).thenReturn(false);
        when(achievementService.getProgress(event.getAuthorId(), achievement.getId())).thenReturn(achievementProgress);

        boolean result = managerAchievementHandler.handleEvent(event);

        verify(achievementService).createProgressIfNecessary(event.getAuthorId(), achievement.getId());
        verify(achievementService).saveProgress(achievementProgress);
        assertFalse(result);
    }

    private TeamEvent prepareEvent() {
        return TeamEvent.builder()
                .id(1L)
                .authorId(2L)
                .projectId(3L)
                .build();
    }
}
