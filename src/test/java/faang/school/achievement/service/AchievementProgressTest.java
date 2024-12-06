package faang.school.achievement.service;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementProgressTest {

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @InjectMocks
    private AchievementProgressService achievementProgressService;

    @Test
    void createProgressIfNecessaryTest() {
        long userId = 1L;
        long achievementId = 2L;

        achievementProgressService.createProgressIfNecessary(userId, achievementId);

        verify(achievementProgressRepository, times(1)).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    void getProgressThrowTest() {
        long userId = 1L;
        long achievementId = 2L;

        assertThrows(EntityNotFoundException.class, () -> achievementProgressService.getProgress(userId, achievementId));
    }

    @Test
    void getProgressTest() {
        long userId = 1L;
        long achievementId = 2L;
        AchievementProgress achievementProgress = new AchievementProgress();
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.of(achievementProgress));

        achievementProgressService.getProgress(userId, achievementId);

        verify(achievementProgressRepository, times(1)).findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void saveProgressTest() {
        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setUserId(1L);
        achievementProgress.setAchievement(new Achievement());

        achievementProgressService.saveProgress(achievementProgress);

        verify(achievementProgressRepository, times(1)).save(achievementProgress);
    }
}
