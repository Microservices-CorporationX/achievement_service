package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.event.AchievementEvent;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.AchievementPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private Cache<Achievement> achievementCache;

    @Spy
    private AchievementMapper achievementMapper = Mappers.getMapper(AchievementMapper.class);

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementPublisher achievementPublisher;

    @InjectMocks
    private AchievementService achievementService;

    @Test
    void getTest() {
        String title = "Achievement1";
        Achievement achievementFirst = new Achievement();
        achievementFirst.setTitle("Achievement1");
        when(achievementCache.get(title)).thenReturn(achievementFirst);

        AchievementDto achievementDto = achievementService.get(title);

        assertEquals(achievementFirst.getTitle(), achievementDto.getTitle());
    }

    @Test
    void getAllTest() {
        Achievement achievementFirst=  new Achievement();
        achievementFirst.setTitle("Achievement1");
        Achievement achievementSecond = new Achievement();
        achievementSecond.setTitle("Achievement2");
        List<Achievement> achievements = List.of(achievementFirst, achievementSecond);
        when(achievementCache.getAll()).thenReturn(achievements);

        List<AchievementDto> achievementDtos = achievementService.getAll();

        assertEquals(achievementFirst.getTitle(), achievementDtos.get(0).getTitle());
        assertEquals(achievementSecond.getTitle(), achievementDtos.get(1).getTitle());
    }

    @Test
    void hasAchievementTest() {
        Long userId = 1L;
        Long achievementId = 2L;
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(true);
        boolean result = achievementService.hasAchievement(userId, achievementId);
        assertTrue(result);
    }

    @Test
    void createProgressIfNecessaryTest() {
        Long userId = 1L;
        Long achievementId = 2L;
        achievementService.createProgressIfNecessary(userId, achievementId);
        verify(achievementProgressRepository).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    void getProgressTest() {
        Long userId = 1L;
        Long achievementId = 2L;
        AchievementProgress expectedProgress = new AchievementProgress();
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.of(expectedProgress));

        AchievementProgress actualProgress = achievementService.getProgress(userId, achievementId);

        assertEquals(expectedProgress, actualProgress);
    }

    @Test
    void throwExceptionProgressNotFoundTest() {
        Long userId = 1L;
        Long achievementId = 2L;
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> achievementService.getProgress(userId, achievementId));
        assertEquals("AchievementProgress not found", exception.getMessage());
    }

    @Test
    void updateProgressTest() {
        AchievementProgress progress = new AchievementProgress();
        progress.setId(1L);

        achievementService.updateProgress(progress);

        verify(achievementProgressRepository).save(progress);
    }

    @Test
    void giveAchievementTest() {
        Long userId = 1L;
        Long achievementId = 2L;
        Achievement achievement = new Achievement();
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));

        achievementService.giveAchievement(userId, achievementId);

        ArgumentCaptor<UserAchievement> userAchievementCaptor = ArgumentCaptor.forClass(UserAchievement.class);
        verify(userAchievementRepository).save(userAchievementCaptor.capture());
        UserAchievement savedAchievement = userAchievementCaptor.getValue();
        assertEquals(userId, savedAchievement.getUserId());
        assertEquals(achievement, savedAchievement.getAchievement());

        ArgumentCaptor<AchievementEvent> eventCaptor = ArgumentCaptor.forClass(AchievementEvent.class);
        verify(achievementPublisher).publish(eventCaptor.capture());
        AchievementEvent publishedEvent = eventCaptor.getValue();
        assertEquals(userId, publishedEvent.getUserId());
        assertEquals(achievementId, publishedEvent.getAchievementId());
    }

    @Test
    void throwExceptionAchievementNotFoundTest() {
        Long achievementId = 2L;
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> achievementService.giveAchievement(1L, achievementId));
        assertEquals("Achievement not found", exception.getMessage());
        verify(achievementRepository).findById(achievementId);
    }
}
