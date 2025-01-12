package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.filter.AchievementFilter;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
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
    private AchievementMapper achievementMapper;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private Cache cache;

    private AchievementFilter achievementFilter;
    private List<AchievementFilter> achievementFilters;

    private long userId;
    private long achievementId;
    private String achievementKey;
    private Achievement senseiAchievement;
    private Achievement firstAchievement;
    private Achievement secondAchievement;
    private Achievement thirdAchievement;
    private List<Achievement> achievements;

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

        firstAchievement = Achievement.builder()
                .id(1L)
                .title("firstAchievement")
                .rarity(Rarity.EPIC)
                .build();

        secondAchievement = Achievement.builder()
                .id(2L)
                .title("secondAchievement")
                .rarity(Rarity.EPIC)
                .build();

        thirdAchievement = Achievement.builder()
                .id(3L)
                .title("thirdAchievement")
                .rarity(Rarity.RARE)
                .build();

        achievements = List.of(
                firstAchievement,
                secondAchievement,
                thirdAchievement
        );

        achievementFilter = Mockito.mock(AchievementFilter.class);
        achievementFilters = List.of(achievementFilter);

        achievementService = new AchievementService(
                userAchievementRepository,
                achievementProgressRepository,
                achievementMapper,
                achievementRepository,
                achievementFilters,
                cache
        );
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

    @Test
    public void testGetAllPossibleAchievements() {
        // arrange
        AchievementFilterDto filterDto = AchievementFilterDto
                .builder()
                .rarity(Rarity.RARE)
                .build();
        List<Achievement> filteredAchievements = List.of(
                firstAchievement,
                thirdAchievement
        );

        when(achievementFilter.isApplicable(filterDto))
                .thenReturn(true);
        when(achievementRepository.findAll())
                .thenReturn(achievements);
        when(achievementFilter.apply(achievements, filterDto))
                .thenReturn(filteredAchievements);

        // act
        achievementService.getAllPossibleAchievements(filterDto);

        // assert
        verify(achievementMapper).toAchievementDto(filteredAchievements);
    }

    @Test
    public void testGetAllUserAchievements() {
        // arrange
        UserAchievement firstUserAchievement = UserAchievement.builder()
                .id(1L)
                .achievement(firstAchievement)
                .build();
        UserAchievement secondUserAchievement = UserAchievement.builder()
                .id(2L)
                .achievement(secondAchievement)
                .build();
        UserAchievement thirdUserAchievement = UserAchievement.builder()
                .id(3L)
                .achievement(thirdAchievement)
                .build();
        List<UserAchievement> userAchievements = List.of(
                firstUserAchievement,
                secondUserAchievement,
                thirdUserAchievement
        );

        when(userAchievementRepository.findByUserId(userId))
                .thenReturn(userAchievements);

        // act
        achievementService.getAllUserAchievements(userId);

        // assert
        verify(achievementMapper).toAchievementDto(achievements);
    }

    @Test
    public void testGetAllUserAchievementsNoAchievementsReturnsEmptyList() {
        // arrange
        List<Achievement> achievements = new ArrayList<>();
        List<UserAchievement> userAchievements = new ArrayList<>();

        when(userAchievementRepository.findByUserId(userId))
                .thenReturn(userAchievements);

        // act
        achievementService.getAllUserAchievements(userId);

        // assert
        verify(achievementMapper).toAchievementDto(achievements);
    }

    @Test
    public void testGetUnfinishedAchievements() {
        // arrange
        AchievementProgress firstAchievementProgress = AchievementProgress.builder()
                .id(1L)
                .userId(userId)
                .currentPoints(30L)
                .build();
        AchievementProgress secondAchievementProgress = AchievementProgress.builder()
                .id(2L)
                .userId(userId)
                .currentPoints(20L)
                .build();
        AchievementProgress thirdAchievementProgress = AchievementProgress.builder()
                .id(3L)
                .userId(userId)
                .currentPoints(40L)
                .build();
        List<AchievementProgress> achievementProgresses = List.of(
                firstAchievementProgress,
                secondAchievementProgress,
                thirdAchievementProgress
        );

        when(achievementProgressRepository.findByUserId(userId))
                .thenReturn(achievementProgresses);

        // act
        achievementService.getUnfinishedUserAchievements(userId);

        // assert
        verify(achievementMapper).toAchievementProgressDto(achievementProgresses);
    }

    @Test
    public void testGetAchievement() {
        // arrange
        long achievementId = 5L;
        Achievement achievement = Achievement.builder()
                .id(achievementId)
                .title("some title")
                .build();
        when(achievementRepository.findById(achievementId))
                .thenReturn(Optional.of(achievement));

        // act
        achievementService.getAchievement(achievementId);

        // assert
        verify(achievementMapper).toAchievementDto(achievement);
    }

    @Test
    public void testGetAchievementThrowsEntityNotFoundException() {
        // arrange
        long achievementId = 5L;
        doThrow(EntityNotFoundException.class)
                .when(achievementRepository)
                .findById(achievementId);

        // act and assert
        assertThrows(EntityNotFoundException.class,
                () -> achievementService.getAchievement(achievementId));
    }
}
