package faang.school.achievement.handler.recommendation.niceguy;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.recommendation.RecommendationEvent;
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
public class NiceGuyAchievementHandlerTest {

    @Mock
    private AchievementCache achievementCache;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private NiceGuyAchievementHandler niceGuyAchievementHandler;

    @Test
    public void testHandleEventIfAchievementNull() {
        RecommendationEvent event = prepareEvent();
        when(achievementCache.get(anyString())).thenReturn(null);

        DataValidationException exception = assertThrows(DataValidationException.class,
                () -> niceGuyAchievementHandler.handleEvent(event));

        assertEquals("Achievement with key 'NICE GUY' not found.", exception.getMessage());
    }

    @Test
    public void testHandleEventIfUserAlreadyHasAchievement() {
        RecommendationEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(10);
        when(achievementCache.get(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getReceiverId(), 1L)).thenReturn(true);

        niceGuyAchievementHandler.handleEvent(event);

        verify(achievementService, never()).createProgressIfNecessary(event.getReceiverId(), achievement.getId());
        verify(achievementService, never()).saveProgress(achievementProgress);
        verify(achievementService, never()).giveAchievement(achievement, event.getReceiverId());
    }

    @Test
    public void testHandleEventIfUserGivenAchievement() {
        RecommendationEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(9);
        when(achievementCache.get(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getReceiverId(), 1L)).thenReturn(false);
        when(achievementService.getProgress(event.getReceiverId(), achievement.getId())).thenReturn(achievementProgress);

        niceGuyAchievementHandler.handleEvent(event);

        verify(achievementService).createProgressIfNecessary(event.getReceiverId(), achievement.getId());
        verify(achievementService).saveProgress(achievementProgress);
        verify(achievementService).giveAchievement(achievement, event.getReceiverId());
    }

    @Test
    public void testHandleEventIfUserNotGivenAchievement() {
        RecommendationEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(8);
        when(achievementCache.get(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getReceiverId(), 1L)).thenReturn(false);
        when(achievementService.getProgress(event.getReceiverId(), achievement.getId())).thenReturn(achievementProgress);

        niceGuyAchievementHandler.handleEvent(event);

        verify(achievementService).createProgressIfNecessary(event.getReceiverId(), achievement.getId());
        verify(achievementService).saveProgress(achievementProgress);
        verify(achievementService, never()).giveAchievement(achievement, event.getReceiverId());
    }

    private RecommendationEvent prepareEvent() {
        return RecommendationEvent.builder()
                .authorId(1L)
                .receiverId(2L)
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
