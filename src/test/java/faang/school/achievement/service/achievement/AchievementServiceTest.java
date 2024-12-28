package faang.school.achievement.service.achievement;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.validator.achievement.AchievementServiceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {
    @InjectMocks
    private AchievementService achievementService;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private AchievementServiceValidator validator;
    private Achievement achievement;
    private AchievementProgress achievementProgress;

    @BeforeEach
    void setUp() {
        achievement = new Achievement();
        achievement.setTitle("title");

        achievementProgress = new AchievementProgress();
        achievementProgress.setId(123);
    }

    @Test
    void testHasAchievementThrownAnException() {
        Mockito.doThrow(IllegalArgumentException.class).when(validator).checkId(-1, -13);

        assertThrows(IllegalArgumentException.class,
                () -> achievementService.hasAchievement(-1, -13));
    }

    @Test
    void testSuccessfulAchievement() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(1, 13)).thenReturn(true);

        assertTrue(achievementService.hasAchievement(1, 13));
    }

    @Test
    void testCreateProgressIfNecessaryThrownAnException() {
        Mockito.doThrow(IllegalArgumentException.class).when(validator).checkId(-1, -13);

        assertThrows(IllegalArgumentException.class,
                () -> achievementService.createProgressIfNecessary(-1, -13));
    }

    @Test
    void testSuccessfulCreateProgressIfNecessary() {
        achievementService.createProgressIfNecessary(1, 13);
        verify(achievementProgressRepository).createProgressIfNecessary(1, 13);
    }

    @Test
    void testGetAchievementByTitleThrownAnException() {
        Mockito.doThrow(IllegalArgumentException.class).when(validator).checkTitle(ArgumentMatchers.anyString());

        assertThrows(IllegalArgumentException.class,
                () -> achievementService.getAchievementByTitleWithOutUserAndProgress(""));
    }

    @Test
    void testSuccessfulGetAchievement() {
        when(achievementRepository.getAchievementByTitle(achievement.getTitle())).thenReturn(achievement);
        achievementService.getAchievementByTitleWithOutUserAndProgress("title");
        verify(achievementRepository).getAchievementByTitle(achievement.getTitle());
    }

    @Test
    void testGetProgressThrownAnException() {
        Mockito.doThrow(IllegalArgumentException.class).when(validator).checkId(-1, -13);

        assertThrows(IllegalArgumentException.class,
                () -> achievementService.getProgress(-1, -13));
    }

    @Test
    void testSuccessfulGetProgress() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(1, 13))
                .thenReturn(Optional.of(achievementProgress));

        AchievementProgress progress = achievementService.getProgress(1, 13);
        verify(achievementProgressRepository).createProgressIfNecessary(1, 13);
        assertEquals(progress.getId(), achievementProgress.getId());
    }

    @Test
    void testCreateNewUserAchievementThrownAnException() {
        Mockito.doThrow(IllegalArgumentException.class).when(validator).checkUserAchievement(null);

        assertThrows(IllegalArgumentException.class,
                () -> achievementService.createNewUserAchievement(null));
    }

    @Test
    void testSuccessCreateNewUserAchievement() {
        UserAchievement userAchievement = new UserAchievement();

        achievementService.createNewUserAchievement(userAchievement);

        verify(userAchievementRepository).save(userAchievement);
    }
}