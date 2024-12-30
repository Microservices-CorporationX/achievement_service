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

    private Achievement setAchievement() {
        return Achievement.builder()
                .id(achievementId)
                .build();
    }
}
