package ru.corporationx.achievement.validator.achievement;

import ru.corporationx.achievement.validator.achievement.AchievementValidator;
import ru.corporationx.achievement.cache.AchievementCache;
import ru.corporationx.achievement.dto.AchievementDto;
import ru.corporationx.achievement.exception.AchievementNotFoundException;
import ru.corporationx.achievement.model.AchievementProgress;
import ru.corporationx.achievement.service.AchievementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementValidatorTest {
    @Mock
    private AchievementCache achievementCache;
    @Mock
    private AchievementService achievementService;
    @InjectMocks
    private AchievementValidator achievementValidator;

    @Test
    public void shouldReturnAchievementWhenExistsInCache() {
        String achievementTitle = "test title";
        AchievementDto achievementDto = AchievementDto.builder()
                .title(achievementTitle)
                .build();
        when(achievementCache.get(achievementTitle)).thenReturn(achievementDto);

        AchievementDto result = achievementValidator.getAndValidateAchievement(achievementTitle);

        verify(achievementCache).get(achievementTitle);
        assertEquals(achievementDto, result);
    }

    @Test
    public void shouldThrowExceptionWhenAchievementNotFound() {
        String achievementTitle = "test title";

        AchievementNotFoundException thrown = Assertions.assertThrows(AchievementNotFoundException.class,
                () -> achievementValidator.getAndValidateAchievement(achievementTitle));
        assertEquals("Failed to get achievement from cache.", thrown.getMessage());
    }

    @Test
    public void shouldCallHasAchievementService() {
        achievementValidator.checkHasAchievement(1L, 1L);
        verify(achievementService).hasAchievement(1L, 1L);
    }

    @Test
    public void shouldIncrementAchievementProgress() {
        AchievementProgress achievementProgress = mock(AchievementProgress.class);
        when(achievementService.getProgress(1L, 1L)).thenReturn(achievementProgress);

        achievementValidator.incrementPoints(1L, 1L);

        verify(achievementProgress).increment();
        verify(achievementService).saveProgress(achievementProgress);
    }

    @Test
    public void shouldGiveAchievementWhenPointsReached() {
        AchievementDto achievementDto = AchievementDto.builder()
                .points(1)
                .build();
        AchievementProgress achievementProgress = AchievementProgress.builder()
                .currentPoints(1)
                .build();

        achievementValidator.giveAchievementIfPointsReached(achievementDto, achievementProgress, 1L);

        verify(achievementService).giveAchievement(achievementDto, 1L);
    }
}