package faang.school.achievement.eventHandler;

import faang.school.achievement.config.SenseiAchievementConfig;
import faang.school.achievement.dto.AchievementCacheDto;
import faang.school.achievement.event.MentorshipAcceptedEvent;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SenseyAchievementHandlerTest {
    @Mock
    private AchievementService achievementService;

    @Mock
    private SenseiAchievementConfig senseiAchievementConfig;

    @InjectMocks
    SenseyAchievementHandler senseyAchievementHandler;

    private MentorshipAcceptedEvent event;
    private AchievementCacheDto achievementCacheDto;
    private AchievementProgress achievementProgress;

    @BeforeEach
    void setUp() {
        event = MentorshipAcceptedEvent.builder()
                .requesterId(1L)
                .receiverId(1l)
                .build();

        achievementCacheDto = AchievementCacheDto.builder()
                .id(1L)
                .points(10)
                .build();

        achievementProgress = AchievementProgress.builder()
                .currentPoints(29)
                .build();

        lenient().when(senseiAchievementConfig.getTitle()).thenReturn("SENSEI");
        lenient().when(senseiAchievementConfig.getRequiredPoints()).thenReturn(30);
    }

    @Test
    @DisplayName("Test handle: user does not have achievement, progress is incremented, and achievement is given when points are reached")
    void testHandleUserDoesNotHaveAchievementProgressIncremented_AchievementGiven() {
        when(achievementService.getAchievementByTitle("SENSEI")).thenReturn(achievementCacheDto);
        when(achievementService.hasAchievement(1L, 1L)).thenReturn(false);
        when(achievementService.getProgress(1L, 1L)).thenReturn(achievementProgress);

        senseyAchievementHandler.handle(event);

        verify(achievementService, times(1)).getAchievementByTitle("SENSEI");
        verify(achievementService, times(1)).hasAchievement(1L, 1L);
        verify(achievementService, times(1)).createProgress(1L, 1L);
        verify(achievementService, times(1)).getProgress(1L, 1L);
        verify(achievementService, times(1)).giveAchievement(1L, 1L);
        verify(achievementService, times(1)).saveProgress(achievementProgress);

        assert achievementProgress.getCurrentPoints() == 30;
    }

    @Test
    @DisplayName("Test handle: user already has achievement, no action taken")
    void testHandleUserAlreadyHasAchievementNoActionTaken() {
        when(achievementService.getAchievementByTitle("SENSEI")).thenReturn(achievementCacheDto);
        when(achievementService.hasAchievement(1L, 1L)).thenReturn(true);

        senseyAchievementHandler.handle(event);

        verify(achievementService, times(1)).getAchievementByTitle("SENSEI");
        verify(achievementService, times(1)).hasAchievement(1L, 1L);
        verify(achievementService, never()).createProgress(anyLong(), anyLong());
        verify(achievementService, never()).getProgress(anyLong(), anyLong());
        verify(achievementService, never()).giveAchievement(anyLong(), anyLong());
        verify(achievementService, never()).saveProgress(any());
    }

    @Test
    @DisplayName("Test handle: user does not have achievement, progress is incremented, but achievement is not given yet")
    void testHandleUserDoesNotHaveAchievementProgressIncrementedAchievementNotGiven() {
        achievementProgress.setCurrentPoints(28);

        when(achievementService.getAchievementByTitle("SENSEI")).thenReturn(achievementCacheDto);
        when(achievementService.hasAchievement(1L, 1L)).thenReturn(false);
        when(achievementService.getProgress(1L, 1L)).thenReturn(achievementProgress);

        senseyAchievementHandler.handle(event);

        verify(achievementService, times(1)).getAchievementByTitle("SENSEI");
        verify(achievementService, times(1)).hasAchievement(1L, 1L);
        verify(achievementService, times(1)).createProgress(1L, 1L);
        verify(achievementService, times(1)).getProgress(1L, 1L);
        verify(achievementService, never()).giveAchievement(anyLong(), anyLong());
        verify(achievementService, times(1)).saveProgress(achievementProgress);

        assert achievementProgress.getCurrentPoints() == 29; // Проверка, что очки увеличились до 29
    }

}