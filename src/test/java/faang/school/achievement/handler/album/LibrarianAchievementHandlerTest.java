package faang.school.achievement.handler.album;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.album.AlbumCreatedEvent;
import faang.school.achievement.exception.DataValidationException;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibrarianAchievementHandlerTest {

    @InjectMocks
    private LibrarianAchievementHandler librarianAchievementHandler;

    @Mock
    private AchievementCache achievementCache;

    @Mock
    private AchievementService achievementService;

    @Value("${spring.data.achievement.librarian}")
    private String achievementKey;

    @Test
    public void testHandleEventIfAchievementNull() {
        AlbumCreatedEvent event = prepareEvent();
        when(achievementCache.get(achievementKey)).thenReturn(null);

        DataValidationException exception = assertThrows(DataValidationException.class,
                () -> librarianAchievementHandler.handleEvent(event));

        assertEquals("Achievement with key " + achievementKey + "  not found", exception.getMessage());
    }

    @Test
    public void testHandleEventIfUserAlreadyHasAchievement() {
        AlbumCreatedEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        when(achievementCache.get(achievementKey)).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getUserId(), achievement.getId())).thenReturn(true);

        boolean result = librarianAchievementHandler.handleEvent(event);

        assertFalse(result);
    }

    @Test
    public void testHandleEventIfUserGivenAchievement() {
        AlbumCreatedEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(9);
        when(achievementCache.get(achievementKey)).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getUserId(), achievement.getId())).thenReturn(false);
        when(achievementService.getProgress(event.getUserId(), achievement.getId())).thenReturn(achievementProgress);

        boolean result = librarianAchievementHandler.handleEvent(event);

        verify(achievementService).createProgressIfNecessary(event.getUserId(), achievement.getId());
        verify(achievementService).saveProgress(achievementProgress);
        verify(achievementService).giveAchievement(achievement, event.getUserId());
        assertTrue(result);
    }

    @Test
    public void testHandleEventIfUserNotGivenAchievement() {
        AlbumCreatedEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(8);
        when(achievementCache.get(achievementKey)).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getUserId(), achievement.getId())).thenReturn(false);
        when(achievementService.getProgress(event.getUserId(), achievement.getId())).thenReturn(achievementProgress);

        boolean result = librarianAchievementHandler.handleEvent(event);

        verify(achievementService).createProgressIfNecessary(event.getUserId(), achievement.getId());
        verify(achievementService).saveProgress(achievementProgress);
        assertFalse(result);
    }

    private AlbumCreatedEvent prepareEvent() {
        return AlbumCreatedEvent.builder()
                .userId(1L)
                .albumId(2L)
                .albumTitle("Test")
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