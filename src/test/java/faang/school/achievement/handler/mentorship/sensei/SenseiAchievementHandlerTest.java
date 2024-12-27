package faang.school.achievement.handler.mentorship.sensei;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.mentorship.MentorshipStartEvent;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.exception.DataValidationException;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SenseiAchievementHandlerTest {

    @Mock
    private AchievementCache achievementCache;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private SenseiAchievementHandler senseiAchievementHandler;

    @Test
    public void testHandleEventIfAchievementNull() {
        MentorshipStartEvent event = prepareEvent();
        when(achievementCache.get(anyString())).thenReturn(null);

        AchievementNotFoundException exception = assertThrows(AchievementNotFoundException.class,
                () -> senseiAchievementHandler.handleEvent(event));

        assertEquals("Failed to get achievement from cache.", exception.getMessage());
    }

    @Test
    public void testHandleEventIfUserAlreadyHasAchievement() {
        MentorshipStartEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(10);
        when(achievementCache.get(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getMentorId(), 1L)).thenReturn(true);

        senseiAchievementHandler.handleEvent(event);

        verify(achievementService, never()).createProgressIfNecessary(event.getMentorId(), achievement.getId());
        verify(achievementService, never()).saveProgress(achievementProgress);
        verify(achievementService, never()).giveAchievement(achievement, event.getMentorId());
    }

    @Test
    public void testHandleEventIfUserGivenAchievement() {
        MentorshipStartEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(9);
        when(achievementCache.get(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getMentorId(), 1L)).thenReturn(false);
        when(achievementService.getProgress(event.getMentorId(), achievement.getId())).thenReturn(achievementProgress);

        senseiAchievementHandler.handleEvent(event);

        verify(achievementService).createProgressIfNecessary(event.getMentorId(), achievement.getId());
        verify(achievementService).saveProgress(achievementProgress);
        verify(achievementService).giveAchievement(achievement, event.getMentorId());
    }

    @Test
    public void testHandleEventIfUserNotGivenAchievement() {
        MentorshipStartEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(8);
        when(achievementCache.get(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getMentorId(), 1L)).thenReturn(false);
        when(achievementService.getProgress(event.getMentorId(), achievement.getId())).thenReturn(achievementProgress);

        senseiAchievementHandler.handleEvent(event);

        verify(achievementService).createProgressIfNecessary(event.getMentorId(), achievement.getId());
        verify(achievementService).saveProgress(achievementProgress);
        verify(achievementService, never()).giveAchievement(achievement, event.getMentorId());
    }

    private MentorshipStartEvent prepareEvent() {
        return MentorshipStartEvent.builder()
                .mentorId(1L)
                .menteeId(2L)
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
