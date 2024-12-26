package faang.school.achievement.service;

import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AchievementProgressServiceTest {

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Mock
    private UserAchievementService userAchievementService;

    @InjectMocks
    private AchievementProgressService achievementProgressService;

    private final long userId = 1L;
    private final long achievementId = 1L;
    private final long invalidUserId = -1L;
    private final long invalidAchievementId = -1L;

    @Test
    void testCreateProgressIfNecessarySuccess() {
        when(userAchievementService.hasAchievement(userId, achievementId)).thenReturn(false);

        achievementProgressService.createProgressIfNecessary(userId, achievementId);

        verify(achievementProgressRepository, times(1)).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    void testCreateProgressIfNecessaryUserIdThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> achievementProgressService.createProgressIfNecessary(invalidUserId, achievementId));

        verify(achievementProgressRepository, never()).save(any());
    }

    @Test
    void testCreateProgressIfNecessaryAchievementIdThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> achievementProgressService.createProgressIfNecessary(userId, invalidAchievementId));

        verify(achievementProgressRepository, never()).save(any());
    }

    @Test
    void testGetProgressSuccess() {
        var achievementProgress = setAchievementProgress();

        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.of(achievementProgress));

        AchievementProgress result = achievementProgressService.getProgress(userId, achievementId);

        assertNotNull(result);
        assertEquals(achievementProgress, result);
        verify(achievementProgressRepository, times(1))
                .findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void testGetProgressThrowEntityNotFoundException() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> achievementProgressService.getProgress(userId, achievementId));
    }


    @Test
    void testGetProgressUserIdThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> achievementProgressService.getProgress(invalidUserId, achievementId));

        verify(achievementProgressRepository, never()).findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void testGetProgressAchievementIdThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> achievementProgressService.getProgress(userId, invalidAchievementId));

        verify(achievementProgressRepository, never()).findByUserIdAndAchievementId(userId, achievementId);
    }

    private AchievementProgress setAchievementProgress() {
        return AchievementProgress.builder()
                .id(1L)
                .build();
    }
}
