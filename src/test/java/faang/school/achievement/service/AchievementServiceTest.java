package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    @InjectMocks
    private AchievementService achievementService;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private Cache cache;

    private long userId;
    private long achievementId;
    private String achievementKey;
    private Achievement senseiAchievement;

    @BeforeEach
    public void setUp() {
        userId = 5L;
        achievementId = 1L;
        achievementKey = "SENSEI";

        senseiAchievement = Achievement.builder()
                .id(achievementId)
                .title(achievementKey)
                .points(15)
                .build();
    }

    @Test
    public void testHandleAchievement() {
        // arrange
        AchievementProgress achievementProgress = AchievementProgress.builder()
                .userId(userId)
                .achievement(senseiAchievement)
                .currentPoints(10)
                .build();

        when(cache.get(achievementKey))
                .thenReturn(new SimpleValueWrapper(senseiAchievement));
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.of(achievementProgress));

        long expectedPointsAfterIncrement = achievementProgress.getCurrentPoints() + 1;

        // act
        achievementService.handleAchievement(userId, achievementKey);

        // assert
        assertEquals(expectedPointsAfterIncrement, achievementProgress.getCurrentPoints());
    }

    @Test
    public void testHandleAchievementUserAlreadyHasAchievement() {
        // arrange
        when(cache.get(achievementKey))
                .thenReturn(new SimpleValueWrapper(senseiAchievement));
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(true);

        // act
        achievementService.handleAchievement(userId, achievementKey);

        // assert
        verify(achievementProgressRepository, times(0))
                .findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    public void testHandleAchievementEnoughPointsToReceiveAchievement() {
        // arrange
        AchievementProgress achievementProgress = AchievementProgress.builder()
                .userId(userId)
                .achievement(senseiAchievement)
                .currentPoints(14)
                .build();
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(senseiAchievement)
                .build();

        when(cache.get(achievementKey))
                .thenReturn(new SimpleValueWrapper(senseiAchievement));
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.of(achievementProgress));

        // act
        achievementService.handleAchievement(userId, achievementKey);

        // assert
        ArgumentCaptor<UserAchievement> userAchievementArgumentCaptor
                = ArgumentCaptor.forClass(UserAchievement.class);
        verify(userAchievementRepository).save(userAchievementArgumentCaptor.capture());
        verify(achievementProgressRepository).delete(achievementProgress);
        assertEquals(userAchievement, userAchievementArgumentCaptor.getValue());
    }
}
