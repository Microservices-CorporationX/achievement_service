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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AchievementProgressServiceTest {

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @InjectMocks
    private AchievementProgressService achievementProgressService;

    private final long userId = 1L;
    private final long achievementId = 1L;

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

    private AchievementProgress setAchievementProgress() {
        return AchievementProgress.builder()
                .id(1L)
                .build();
    }
}
