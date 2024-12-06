package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.AlbumCreatedEvent;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementProgressService;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Mock
    private AchievementProgressService achievementProgressService;

    @InjectMocks
    private LibrarianAchievementHandler librarianAchievementHandler;

    @Test
    void getAchievementTest() {
        String title = "Test";
        Achievement achievement = new Achievement();
        achievement.setTitle(title);
        when(achievementCache.get(title)).thenReturn(achievement);

        Achievement result = librarianAchievementHandler.getAchievement(title);

        assertEquals(achievement.getTitle(), result.getTitle());
    }

    @Test
    void requiredEventTest() {
        Class<?> albumCreated = AlbumCreatedEvent.class;

        Class<?> result = librarianAchievementHandler.requiredEvent();

        assertEquals(albumCreated, result);
    }

    @Test
    void handleAchievementHasAchievementTest() {
        long userId = 1L;
        String titleAchievement = "Test";
        Achievement achievement = new Achievement();
        achievement.setTitle(titleAchievement);
        achievement.setId(2L);
        when(achievementCache.get("Test")).thenReturn(achievement);
        when(achievementService.hasAchievement(userId,achievement.getId())).thenReturn(true);

        librarianAchievementHandler.handleAchievement(userId, titleAchievement);

        verify(achievementProgressService, times(0))
                .createProgressIfNecessary(userId, achievement.getId());
    }

    @Test
    void handleAchievementNotEnoughPointsTest() {
        long userId = 1L;
        String titleAchievement = "Test";
        Achievement achievement = new Achievement();
        achievement.setTitle(titleAchievement);
        achievement.setId(2L);
        achievement.setPoints(10);
        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(5);
        when(achievementCache.get("Test")).thenReturn(achievement);
        when(achievementService.hasAchievement(userId,achievement.getId())).thenReturn(false);
        when(achievementProgressService.getProgress(userId, achievement.getId())).thenReturn(achievementProgress);

        librarianAchievementHandler.handleAchievement(userId, titleAchievement);

        verify(achievementService, times(0))
                .giveAchievement(any());
    }

    @Test
    void handleAchievementTest() {
        long userId = 1L;
        String titleAchievement = "Test";
        Achievement achievement = new Achievement();
        achievement.setTitle(titleAchievement);
        achievement.setId(2L);
        achievement.setPoints(10);
        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(9);
        when(achievementCache.get("Test")).thenReturn(achievement);
        when(achievementService.hasAchievement(userId,achievement.getId())).thenReturn(false);
        when(achievementProgressService.getProgress(userId, achievement.getId())).thenReturn(achievementProgress);

        librarianAchievementHandler.handleAchievement(userId, titleAchievement);

        verify(achievementService, times(1))
                .giveAchievement(any());
    }
}
