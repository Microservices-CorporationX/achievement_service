package faang.school.achievement.handler.team.manager;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.exception.DataValidationException;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.dto.team.TeamEvent;
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

        AchievementNotFoundException exception = assertThrows(AchievementNotFoundException.class,
                () -> managerAchievementHandler.handleEvent(event));

        assertEquals("Failed to get 'MANAGER' achievement from cache.", exception.getMessage());
    }

    @Test
    public void testHandleEventIfUserAlreadyHasAchievement() {
        TeamEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(10);
        when(achievementCache.get(anyString())).thenReturn(achievement);
        when(achievementService.getProgress(event.getAuthorId(), achievement.getId())).thenReturn(achievementProgress);

        boolean result = managerAchievementHandler.handleEvent(event);

        assertFalse(result);
    }

    @Test
    public void testHandleEventIfUserGivenAchievement() {
        TeamEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(9);
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
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(8);
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

    private AchievementDto prepareAchievementDto() {
        return AchievementDto.builder()
                .id(1L)
                .points(10L)
                .build();
    }

    private AchievementProgress prepareAchievementProgress(long currentPoints) {
        return AchievementProgress.builder()
                .id(2L)
                .currentPoints(currentPoints)
                .build();
    }
}
