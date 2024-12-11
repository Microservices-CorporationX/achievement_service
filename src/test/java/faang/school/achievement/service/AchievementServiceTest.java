package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @InjectMocks
    private AchievementService achievementService;

    @Test
    public void hasAchievementTrue() {
        long userId = 1L;
        long achievementId = 2L;

        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(true);

        boolean result = achievementService.hasAchievement(userId, achievementId);

        verify(userAchievementRepository).existsByUserIdAndAchievementId(userId, achievementId);
        assertTrue(result);
    }

    @Test
    public void hasAchievementFalse() {
        long userId = 1L;
        long achievementId = 2L;

        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(false);

        boolean result = achievementService.hasAchievement(userId, achievementId);

        verify(userAchievementRepository).existsByUserIdAndAchievementId(userId, achievementId);
        assertFalse(result);
    }

    @Test
    public void createProgressIfNecessaryTest() {
        long userId = 1L;
        long achievementId = 2L;

        doNothing().when(achievementProgressRepository).createProgressIfNecessary(userId, achievementId);

        achievementService.createProgressIfNecessary(userId, achievementId);

        verify(achievementProgressRepository).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    public void getProgressTest() {
        long userId = 1L;
        long achievementId = 2L;
        AchievementProgress achievementProgress = new AchievementProgress();

        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.of(achievementProgress));

        achievementService.getProgress(userId, achievementId);

        verify(achievementProgressRepository).findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    public void getProgressThrowsExceptionTest() {
        long userId = 1L;
        long achievementId = 2L;

        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> achievementService.getProgress(userId, achievementId));
    }

    @Test
    public void saveProgressTest() {
        AchievementProgress achievementProgress = new AchievementProgress();

        achievementService.saveProgress(achievementProgress);

        verify(achievementProgressRepository).save(achievementProgress);
    }

    @Test
    public void giveAchievementTest() {
        Achievement achievement = new Achievement();
        achievement.setTitle("test");

        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setUserId(1L);
        userAchievement.setAchievement(achievement);

        achievementService.giveAchievement(userAchievement);

        verify(userAchievementRepository).save(userAchievement);
    }

}
