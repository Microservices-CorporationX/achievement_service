package faang.school.achievement.service;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {
    @Mock
    private AchievementCache achievementCache;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @InjectMocks
    private AchievementService achievementService;

    @Test
    void getAchievementByTitleSuccessTest() {
        Achievement collector = Achievement.builder()
                .title("COLLECTOR")
                .rarity(Rarity.RARE)
                .points(15)
                .build();
        when(achievementCache.getAchievementByTitle("COLLECTOR")).thenReturn(collector);
        assertDoesNotThrow(() -> {
            Achievement achievement = achievementService.getAchievementByTitle("COLLECTOR");
            assertEquals(collector, achievement);
        });
        verify(achievementCache).getAchievementByTitle("COLLECTOR");
    }

    @Test
    void getAchievementByTitleForNonExistentAchievementFailTest() {
        when(achievementCache.getAchievementByTitle("COLLECTOR")).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> achievementService.getAchievementByTitle("COLLECTOR"));
        verify(achievementCache).getAchievementByTitle("COLLECTOR");
    }

    @Test
    void hasAchievementSuccessTest() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(1, 1)).thenReturn(true);
        assertDoesNotThrow(() -> {
            assertTrue(achievementService.hasAchievement(1, 1));
        });
        verify(userAchievementRepository).existsByUserIdAndAchievementId(1, 1);
    }

    @Test
    void createProgressIfNecessarySuccessTest() {
        assertDoesNotThrow(() -> achievementService.createProgressIfNecessary(1L, 1L));
        verify(achievementProgressRepository).createProgressIfNecessary(1, 1);
    }

    @Test
    void getProgressSuccessTest() {
        AchievementProgress collectorProgress = AchievementProgress.builder()
                .id(1)
                .userId(1)
                .currentPoints(10)
                .build();
        when(achievementProgressRepository.findByUserIdAndAchievementId(1, 1)).thenReturn(Optional.of(collectorProgress));
        assertDoesNotThrow(() -> {
            AchievementProgress progress = achievementService.getProgress(1L, 1L);
            assertEquals(collectorProgress, progress);
        });
        verify(achievementProgressRepository).findByUserIdAndAchievementId(1, 1);
    }

    @Test
    void getProgressForNonExistentProgressFailTest() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(1, 1)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> achievementService.getProgress(1L, 1L));
        verify(achievementProgressRepository).findByUserIdAndAchievementId(1, 1);
    }

    @Test
    void giveAchievementSuccessTest() {
        UserAchievement achievement = UserAchievement.builder()
                .id(1)
                .userId(1)
                .build();
        assertDoesNotThrow(() -> achievementService.giveAchievement(achievement));
        verify(userAchievementRepository).save(achievement);
    }

    @Test
    void updateProgressSuccessTest() {
        AchievementProgress progress = AchievementProgress.builder()
                .id(1)
                .userId(1)
                .currentPoints(10)
                .build();
        assertDoesNotThrow(() -> achievementService.updateProgress(progress));
        verify(achievementProgressRepository).save(progress);
    }
}