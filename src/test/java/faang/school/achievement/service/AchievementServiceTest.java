package faang.school.achievement.service;

import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.achievement.AchievementDto;
import faang.school.achievement.dto.achievement.AchievementProgressDto;
import faang.school.achievement.dto.achievement.UserAchievementDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.mapper.UserAchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementMapper achievementMapper;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Mock
    private UserAchievementMapper userAchievementMapper;

    @Mock
    private AchievementProgressMapper achievementProgressMapper;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private AchievementService achievementService;


    @Test
    @DisplayName("Получение достижений пользователя: успех")
    void getAchievementsByUserId_Success() {
        long userId = 1L;
        UserAchievement userAchievement = new UserAchievement();
        UserAchievementDto userAchievementDto = new UserAchievementDto(1L, 2L, userId);

        when(userContext.getUserId()).thenReturn(userId);
        when(userAchievementRepository.findByUserId(userId)).thenReturn(List.of(userAchievement));
        when(userAchievementMapper.toDto(userAchievement)).thenReturn(userAchievementDto);

        List<UserAchievementDto> result = achievementService.getAchievementsByUserId();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userAchievementDto, result.get(0));
        verify(userAchievementRepository, times(1)).findByUserId(userId);
    }


    @Test
    @DisplayName("Получение достижений пользователя: пользователь не найден")
    void getAchievementsByUserId_UserNotFound() {
        long userId = 1L;

        when(userContext.getUserId()).thenReturn(userId);
        when(userAchievementRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<UserAchievementDto> result = achievementService.getAchievementsByUserId();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userAchievementRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Получение достижения по ID: успех")
    void getAchievementById_Success() {
        long id = 1L;
        Achievement achievement = new Achievement();
        AchievementDto achievementDto = new AchievementDto();

        when(achievementRepository.findById(id)).thenReturn(Optional.of(achievement));
        when(achievementMapper.toDto(achievement)).thenReturn(achievementDto);

        AchievementDto result = achievementService.getAchievementById(id);

        assertNotNull(result);
        assertEquals(achievementDto, result);
        verify(achievementRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Получение достижения по ID: не найдено")
    void getAchievementById_NotFound() {
        long id = 1L;

        when(achievementRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
            achievementService.getAchievementById(id));

        assertEquals("Achievement with ID: 1 not found", exception.getMessage());
        verify(achievementRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Получение прогресса достижений пользователя: успех")
    void getAchievementProgressByUserId_Success() {
        long userId = 1L;
        AchievementProgress progress = new AchievementProgress();

        AchievementProgressDto progressDto = new AchievementProgressDto(1L, 2L, 3L, 4L);

        when(userContext.getUserId()).thenReturn(userId);
        when(achievementProgressRepository.findByUserId(userId)).thenReturn(List.of(progress));
        when(achievementProgressMapper.toDto(progress)).thenReturn(progressDto);

        List<AchievementProgressDto> result = achievementService.getAchievementProgressByUserId();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(progressDto, result.get(0));
        verify(achievementProgressRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Получение прогресса достижений пользователя: пользователь не найден")
    void getAchievementProgressByUserId_UserNotFound() {
        long userId = 1L;

        when(userContext.getUserId()).thenReturn(userId);
        when(achievementProgressRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<AchievementProgressDto> result = achievementService.getAchievementProgressByUserId();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(achievementProgressRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Создание прогресса достижения при необходимости: успех")
    void createProgressIfNecessary_Success() {
        long userId = 1L;
        long achievementId = 1L;

        doNothing().when(achievementProgressRepository).createProgressIfNecessary(userId, achievementId);

        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);

        verify(achievementProgressRepository, times(1)).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    @DisplayName("Проверка существования пользовательского достижения: успех")
    void checkUserAchievementExists_Success() {
        long userId = 1L;
        long achievementId = 1L;

        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(true);

        boolean exists = userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);

        assertTrue(exists);
        verify(userAchievementRepository, times(1)).existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    @DisplayName("Проверка существования пользовательского достижения: не найдено")
    void checkUserAchievementExists_NotFound() {
        long userId = 1L;
        long achievementId = 1L;

        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(false);

        boolean exists = userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);

        assertFalse(exists);
        verify(userAchievementRepository, times(1)).existsByUserIdAndAchievementId(userId, achievementId);
    }
}
