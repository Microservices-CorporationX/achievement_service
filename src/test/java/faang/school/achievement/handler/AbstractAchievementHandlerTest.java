package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.event.AlbumCreatedEvent;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AbstractAchievementHandlerTest {

    @Mock
    private AchievementCache achievementCache;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private LibrarianAchievementHandler librarianAchievementHandler;

    @Test
    void handleAchievementHasAchievementTest() {
        AlbumCreatedEvent event = new AlbumCreatedEvent();
        event.setUserId(1L);
        String titleAchievement = "Test";
        Achievement achievement = new Achievement();
        achievement.setTitle(titleAchievement);
        achievement.setId(2L);
        when(achievementCache.get("Test")).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getUserId(),achievement.getId())).thenReturn(true);

        librarianAchievementHandler.handleAchievement(event, titleAchievement);

        verify(achievementService, times(0))
                .createProgressIfNecessary(event.getUserId(), achievement.getId());
    }

    @Test
    void handleAchievementNotEnoughPointsTest() {
        AlbumCreatedEvent event = new AlbumCreatedEvent();
        event.setUserId(1L);
        String titleAchievement = "Test";
        Achievement achievement = new Achievement();
        achievement.setTitle(titleAchievement);
        achievement.setId(2L);
        achievement.setPoints(10);
        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(5);
        when(achievementCache.get("Test")).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getUserId(),achievement.getId())).thenReturn(false);
        when(achievementService.getProgress(event.getUserId(), achievement.getId())).thenReturn(achievementProgress);

        librarianAchievementHandler.handleAchievement(event, titleAchievement);

        verify(achievementService, times(0))
                .giveAchievement(any());
    }

    @Test
    void handleAchievementTest() {
        AlbumCreatedEvent event = new AlbumCreatedEvent();
        event.setUserId(1L);
        String titleAchievement = "Test";
        Achievement achievement = new Achievement();
        achievement.setTitle(titleAchievement);
        achievement.setId(2L);
        achievement.setPoints(10);
        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(9);
        when(achievementCache.get("Test")).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getUserId(),achievement.getId())).thenReturn(false);
        when(achievementService.getProgress(event.getUserId(), achievement.getId())).thenReturn(achievementProgress);

        librarianAchievementHandler.handleAchievement(event, titleAchievement);

        verify(achievementService, times(1))
                .giveAchievement(any());
    }
}
