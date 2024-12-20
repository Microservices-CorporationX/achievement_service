package faang.school.achievement.service.achievement;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.AchievementPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    AchievementProgressRepository achievementProgressRepository;

    @Mock
    UserAchievementRepository userAchievementRepository;

    @Spy
    private AchievementMapper achievementMapper = Mappers.getMapper(AchievementMapper.class);

    @Mock
    private AchievementCache achievementCache;

    @Mock
    AchievementRepository achievementRepository;

    @Mock
    AchievementPublisher achievementPublisher;

    @InjectMocks
    AchievementService achievementService;

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
    public void hasAchievementTest() {
        long achievementId = 1L;
        long userId = 2L;

        when(userAchievementRepository.
                existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(true);

        achievementService.hasAchievement(userId, achievementId);

        verify(userAchievementRepository, times(1)).
                existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    public void createProgressIfNecessaryTest() {
        long achievementId = 1L;
        long userId = 2L;

        doNothing().when(achievementProgressRepository).
                createProgressIfNecessary(userId, achievementId);

        achievementService.createProgressIfNecessary(userId, achievementId);

        verify(achievementProgressRepository, times(1)).
                createProgressIfNecessary(userId, achievementId);
    }

    @Test
    public void getProgressTest() {
        long achievementId = 1L;
        long userId = 2L;
        AchievementProgress achievementProgress = AchievementProgress.builder().build();

        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)).
                thenReturn(Optional.ofNullable(achievementProgress));

        achievementService.getProgress(userId, achievementId);

        verify(achievementProgressRepository, times(1)).
                findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    public void getProgressThrowsExceptionTest() {
        long achievementId = 1L;
        long userId = 2L;

        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> achievementService.getProgress(userId, achievementId));
    }

    @Test
    public void saveProgressTest() {
        AchievementProgress achievementProgress = AchievementProgress.builder().build();

        achievementService.updateProgress(achievementProgress);

        verify(achievementProgressRepository, times(1)).save(achievementProgress);
    }

    @Test
    public void giveAchievementTest() {
        long userId = 1L;
        long achievementId = 1L;
        Achievement achievement = Achievement.builder()
                .id(achievementId)
                .title("Title")
                .build();
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.ofNullable(achievement));

        achievementService.giveAchievement(userId, achievementId);

        verify(userAchievementRepository, times(1)).save(userAchievement);
    }
}