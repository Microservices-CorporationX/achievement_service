package ru.corporationx.achievement.handler.subscription.followers;

import ru.corporationx.achievement.cache.AchievementCache;
import ru.corporationx.achievement.dto.AchievementDto;
import ru.corporationx.achievement.dto.subscription.FollowerEvent;
import ru.corporationx.achievement.exception.AchievementNotFoundException;
import ru.corporationx.achievement.model.AchievementProgress;
import ru.corporationx.achievement.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.corporationx.achievement.handler.subscription.followers.FollowersAchievementHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowersAchievementHandlerTest {

    @Mock
    private AchievementCache achievementCache;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private FollowersAchievementHandler followersAchievementHandler;

    @Test
    public void testHandleEventIfAchievementNull() {
        FollowerEvent event = prepareEvent();
        when(achievementCache.get(anyString())).thenReturn(null);

        AchievementNotFoundException exception = assertThrows(AchievementNotFoundException.class,
                () -> followersAchievementHandler.handleEvent(event));

        assertEquals("Failed to get achievement from cache.", exception.getMessage());
    }

    @Test
    public void testHandleEventIfUserAlreadyHasAchievement() {
        FollowerEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(100);
        when(achievementCache.get(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getFolloweeId(), achievement.getId())).thenReturn(true);

        followersAchievementHandler.handleEvent(event);

        verify(achievementService, never()).createProgressIfNecessary(event.getFolloweeId(), achievement.getId());
        verify(achievementService, never()).saveProgress(achievementProgress);
        verify(achievementService, never()).giveAchievement(achievement, event.getFolloweeId());
    }

    @Test
    public void testHandleEventIfUserGivenAchievement() {
        FollowerEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(99);
        when(achievementCache.get(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getFolloweeId(), achievement.getId())).thenReturn(false);
        when(achievementService.getProgress(event.getFolloweeId(), achievement.getId())).thenReturn(achievementProgress);

        followersAchievementHandler.handleEvent(event);

        verify(achievementService).createProgressIfNecessary(event.getFolloweeId(), achievement.getId());
        verify(achievementService).saveProgress(achievementProgress);
        verify(achievementService).giveAchievement(achievement, event.getFolloweeId());
    }

    @Test
    public void testHandleEventIfUserNotGivenAchievement() {
        FollowerEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(98);
        when(achievementCache.get(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getFolloweeId(), achievement.getId())).thenReturn(false);
        when(achievementService.getProgress(event.getFolloweeId(), achievement.getId())).thenReturn(achievementProgress);

        followersAchievementHandler.handleEvent(event);

        verify(achievementService).createProgressIfNecessary(event.getFolloweeId(), achievement.getId());
        verify(achievementService).saveProgress(achievementProgress);
        verify(achievementService, never()).giveAchievement(achievement, event.getFolloweeId());
    }

    private FollowerEvent prepareEvent() {
        return new FollowerEvent(1L, 2L);
    }

    private AchievementDto prepareAchievementDto() {
        return AchievementDto.builder()
                .id(3L)
                .points(100L)
                .build();
    }

    private AchievementProgress prepareAchievementProgress(long currentPoints) {
        return AchievementProgress.builder()
                .id(2L)
                .currentPoints(currentPoints)
                .build();
    }
}
