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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    private AchievementService achievementService;

    @Mock
    private AchievementMapper achievementMapper;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    private AchievementFilter achievementFilter;
    private List<AchievementFilter> achievementFilters;

    private long userId;
    private Achievement firstAchievement;
    private Achievement secondAchievement;
    private Achievement thirdAchievement;
    private List<Achievement> achievements;

    @BeforeEach
    public void setUp() {
        achievementFilter = Mockito.mock(AchievementFilter.class);
        achievementFilters = List.of(achievementFilter);

        achievementService = new AchievementService(
                achievementMapper,
                achievementRepository,
                achievementProgressRepository,
                userAchievementRepository,
                achievementFilters
        );

        userId = 5L;

        firstAchievement = Achievement.builder()
                .id(1L)
                .title("firstAchievement")
                .rarity(Rarity.RARE)
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
