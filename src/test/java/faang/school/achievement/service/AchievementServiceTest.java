package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.DataValidationException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementMapperImpl;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Spy
    private AchievementMapperImpl achievementMapper;

    @InjectMocks
    private AchievementService achievementService;

    private long userId;
    private long achievementId;

    @BeforeEach
    public void setUp() {
        userId = 1L;
        achievementId = 2L;
    }

    @Test
    public void testHasAchievement() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(true);

        boolean result = achievementService.hasAchievement(userId, achievementId);

        assertTrue(result);
    }

    @Test
    public void testHasNotAchievement() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(false);

        boolean result = achievementService.hasAchievement(userId, achievementId);

        assertFalse(result);
    }

    @Test
    public void testCreateProgressIfNecessary() {
        achievementService.createProgressIfNecessary(userId, achievementId);

        verify(achievementProgressRepository).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    public void testGetProgress() {
        AchievementProgress progress = AchievementProgress.builder().build();
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)).thenReturn(Optional.of(progress));

        AchievementProgress actualProgress = achievementService.getProgress(userId, achievementId);

        verify(achievementProgressRepository).findByUserIdAndAchievementId(userId, achievementId);
        assertEquals(progress, actualProgress);
    }

    @Test
    public void testDoNotGetProgress() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)).thenReturn(Optional.empty());

        DataValidationException exception = assertThrows(DataValidationException.class,
                () -> achievementService.getProgress(userId, achievementId));

        verify(achievementProgressRepository).findByUserIdAndAchievementId(userId, achievementId);
        assertEquals("Achievement progress not found for userId: 1 and achievementId: 2", exception.getMessage());
    }

    @Test
    public void testSaveProgress() {
        AchievementProgress progress = AchievementProgress.builder().build();
        achievementService.saveProgress(progress);

        verify(achievementProgressRepository).save(progress);
    }

    @Test
    public void testGiveAchievement() {
        AchievementDto achievement = AchievementDto.builder().build();
        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievementMapper.toEntity(achievement))
                .userId(userId)
                .build();
        achievementService.giveAchievement(achievement, userId);

        verify(userAchievementRepository).save(userAchievement);
    }
}
