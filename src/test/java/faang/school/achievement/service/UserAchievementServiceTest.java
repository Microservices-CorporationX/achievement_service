package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAchievementServiceTest {

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @InjectMocks
    private UserAchievementService userAchievementService;

    private final long userId = 1L;
    private final long achievementId = 1L;

    @Test
    void testHasAchievementReturnFalse() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(false);

        boolean result = userAchievementService.hasAchievement(userId, achievementId);

        assertFalse(result);
    }

    @Test
    void testHasAchievementReturnTrue() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(true);

        boolean result = userAchievementService.hasAchievement(userId, achievementId);

        assertTrue(result);
    }

    @Test
    void testHasAchievementUserIdThrowIllegalArgumentException() {
        long invalidUserId = -1L;

        assertThrows(IllegalArgumentException.class,
                () -> userAchievementService.hasAchievement(invalidUserId, achievementId));

        verify(userAchievementRepository, never()).save(any());
    }

    @Test
    void testHasAchievementAchievementIdThrowIllegalArgumentException() {
        long invalidAchievementId = -1L;

        assertThrows(IllegalArgumentException.class,
                () -> userAchievementService.hasAchievement(userId, invalidAchievementId));

        verify(userAchievementRepository, never()).save(any());
    }

    @Test
    void testGiveAchievementSuccess() {
        var achievement = setAchievement();

        userAchievementService.giveAchievement(userId, achievement);

        ArgumentCaptor<UserAchievement> captor = ArgumentCaptor.forClass(UserAchievement.class);
        verify(userAchievementRepository).save(captor.capture());

        UserAchievement saved = captor.getValue();
        assertEquals(userId, saved.getUserId());
        assertEquals(achievement, saved.getAchievement());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void testGiveAchievementUserIdThrowIllegalArgumentException() {
        long invalidUserId = -1L;
        Achievement achievement = setAchievement();

        assertThrows(IllegalArgumentException.class,
                () -> userAchievementService.giveAchievement(invalidUserId, achievement));

        verify(userAchievementRepository, never()).save(any());
    }

    @Test
    void testGiveAchievementAchievementThrowIllegalArgumentException() {
        Achievement nullAchievement = null;

        assertThrows(IllegalArgumentException.class,
                () -> userAchievementService.giveAchievement(userId, nullAchievement));

        verify(userAchievementRepository, never()).save(any());
    }

    private Achievement setAchievement() {
        return Achievement.builder()
                .id(achievementId)
                .build();
    }
}
