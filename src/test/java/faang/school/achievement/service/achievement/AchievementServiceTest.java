package faang.school.achievement.service.achievement;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    AchievementProgressRepository achievementProgressRepository;

    @Mock
    UserAchievementRepository achievementRepository;

    @InjectMocks
    AchievementService achievementService;

    @Test
    public void hasAchievementTest(){
        long achievementId = 1L;
        long userId = 2L;

        when(achievementRepository.
                existsByUserIdAndAchievementId(userId,achievementId)).thenReturn(true);

        achievementService.hasAchievement(userId,achievementId);

        verify(achievementRepository,times(1)).
                existsByUserIdAndAchievementId(userId,achievementId);
    }

    @Test
    public void createProgressIfNecessaryTest(){
        long achievementId = 1L;
        long userId = 2L;

        doNothing().when(achievementProgressRepository).
                createProgressIfNecessary(userId,achievementId);

        achievementService.createProgressIfNecessary(userId,achievementId);

        verify(achievementProgressRepository,times(1)).
                createProgressIfNecessary(userId,achievementId);
    }

    @Test
    public void getProgressTest(){
        long achievementId = 1L;
        long userId = 2L;
        AchievementProgress achievementProgress = AchievementProgress.builder().build();

        when(achievementProgressRepository.findByUserIdAndAchievementId(userId,achievementId)).
                thenReturn(Optional.ofNullable(achievementProgress));

        achievementService.getProgress(userId,achievementId);

        verify(achievementProgressRepository,times(1)).
                findByUserIdAndAchievementId(userId,achievementId);
    }

    @Test
    public void getProgressThrowsExceptionTest(){
        long achievementId = 1L;
        long userId = 2L;

        when(achievementProgressRepository.findByUserIdAndAchievementId(userId,achievementId))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> achievementService.getProgress(userId,achievementId));
    }

    @Test
    public void saveProgressTest(){
        AchievementProgress achievementProgress = AchievementProgress.builder().build();

        achievementService.saveProgress(achievementProgress);

        verify(achievementProgressRepository,times(1)).save(achievementProgress);
    }

    @Test
    public void giveAchievementTest(){
        Achievement achievement = Achievement.builder()
                .title("Title")
                .build();
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(1L)
                .achievement(achievement)
                .build();

        achievementService.giveAchievement(userAchievement);

        verify(achievementRepository,times(1)).save(userAchievement);
    }
}