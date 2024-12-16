package faang.school.achievement.handlertests;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.handler.HandsomeAchievementHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class HandsomeAchievementHandlerTest {

    private final AchievementCache achievementCache = mock(AchievementCache.class);
    private final AchievementService achievementService = mock(AchievementService.class);
    private final HandsomeAchievementHandler handler = new HandsomeAchievementHandler(achievementCache, achievementService);

    @Test
    void testUserAlreadyHasAchievement() {
        ProfilePicEvent event = new ProfilePicEvent(1L, "avatar.png");
        Achievement achievement = new Achievement(1L, "Handsome", "Be Handsome", null, null, null, 10, null, null);

        when(achievementCache.getAchievement("Handsome")).thenReturn(Optional.of(achievement));
        when(achievementService.hasAchievement(1L, 1L)).thenReturn(true);

        handler.handleEvent(event);

        verify(achievementService, never()).giveAchievement(anyLong(), any());
        verify(achievementService, never()).createProgressIfNecessary(anyLong(), anyLong());
    }

    @Test
    void testUserDoesNotHaveAchievementButDoesNotReachGoal() {
        ProfilePicEvent event = new ProfilePicEvent(1L, "avatar.png");
        Achievement achievement = new Achievement(1L, "Handsome", "Be Handsome", null, null, null, 10, null, null);
        AchievementProgress progress = new AchievementProgress(1L, achievement, 1L, 8, null, null, 0);

        when(achievementCache.getAchievement("Handsome")).thenReturn(Optional.of(achievement));
        when(achievementService.hasAchievement(1L, 1L)).thenReturn(false);
        when(achievementService.getProgress(1L, 1L)).thenReturn(progress);

        handler.handleEvent(event);

        verify(achievementService, never()).giveAchievement(anyLong(), any());
    }

    @Test
    void testUserDoesNotHaveAchievementAndReachesGoal() {
        ProfilePicEvent event = new ProfilePicEvent(1L, "avatar.png");
        Achievement achievement = new Achievement(1L, "Handsome", "Be Handsome", null, null, null, 10, null, null);
        AchievementProgress progress = new AchievementProgress(1L, achievement, 1L, 9, null, null, 0);

        when(achievementCache.getAchievement("Handsome")).thenReturn(Optional.of(achievement));
        when(achievementService.hasAchievement(1L, 1L)).thenReturn(false);
        when(achievementService.getProgress(1L, 1L)).thenReturn(progress);

        handler.handleEvent(event);

        verify(achievementService, times(1)).giveAchievement(1L, achievement);
    }

    @Test
    void testAchievementNotFound() {
        ProfilePicEvent event = new ProfilePicEvent(1L, "avatar.png");

        when(achievementCache.getAchievement("Handsome")).thenReturn(Optional.empty());

        handler.handleEvent(event);

        verify(achievementService, never()).giveAchievement(anyLong(), any());
        verify(achievementService, never()).createProgressIfNecessary(anyLong(), anyLong());
    }
}