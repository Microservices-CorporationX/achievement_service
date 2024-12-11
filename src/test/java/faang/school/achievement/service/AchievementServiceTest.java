package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.mapper.achievement.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Spy
    private AchievementMapper achievementMapper = Mappers.getMapper(AchievementMapper.class);

    @Mock
    private AchievementCache achievementCache;

    @InjectMocks
    private AchievementService achievementService;


    @Test
    void getTest() {
        Achievement achievementFirst = new Achievement();
        achievementFirst.setTitle("Achievement");
        when(achievementCache.get("Achievement")).thenReturn(achievementFirst);

        AchievementDto achievementDto = achievementService.get("Achievement");

        assertEquals(achievementFirst.getTitle(), achievementDto.getTitle());
    }

    @Test
    void getAllTest() {
        Achievement achievementFirst = new Achievement();
        achievementFirst.setTitle("Achievement1");
        Achievement achievementSecond = new Achievement();
        achievementSecond.setTitle("Achievement2");
        when(achievementCache.getAll()).thenReturn(List.of(achievementFirst, achievementSecond));

        List<AchievementDto> result = achievementService.getAll();

        assertEquals(2, result.size());
        assertEquals(achievementFirst.getTitle(), result.get(0).getTitle());
        assertEquals(achievementSecond.getTitle(), result.get(1).getTitle());
    }

    @Test
    void hasAchievementTest() {
        long userId = 1L;
        long achievementId = 2L;
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(true);

        boolean result = achievementService.hasAchievement(userId, achievementId);

        assertTrue(result);
    }

    @Test
    void giveAchievementTest() {
        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setUserId(1L);
        userAchievement.setAchievement(new Achievement());

        achievementService.giveAchievement(userAchievement);

        verify(userAchievementRepository, times(1)).save(userAchievement);
    }

    @Test
    void createProgressIfNecessaryTest() {
        long userId = 1L;
        long achievementId = 2L;

        achievementService.createProgressIfNecessary(userId, achievementId);

        verify(achievementProgressRepository, times(1)).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    void getProgressThrowTest() {
        long userId = 1L;
        long achievementId = 2L;

        assertThrows(EntityNotFoundException.class, () -> achievementService.getProgress(userId, achievementId));
    }

    @Test
    void getProgressTest() {
        long userId = 1L;
        long achievementId = 2L;
        AchievementProgress achievementProgress = new AchievementProgress();
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.of(achievementProgress));

        achievementService.getProgress(userId, achievementId);

        verify(achievementProgressRepository, times(1)).findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void saveProgressTest() {
        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setUserId(1L);
        achievementProgress.setAchievement(new Achievement());

        achievementService.saveProgress(achievementProgress);

        verify(achievementProgressRepository, times(1)).save(achievementProgress);
    }
}