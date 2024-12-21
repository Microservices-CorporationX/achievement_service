package faang.school.achievement.service;

import faang.school.achievement.event.AchievementEvent;
import faang.school.achievement.exception.AchievementAlreadyExistsException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.AchievementEventPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {
    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private AchievementEventPublisher achievementEventPublisher;

    @InjectMocks
    private AchievementService achievementService;

    private Achievement achievement;

    @BeforeEach
    void setUp() {
        achievement = Achievement.builder()
                .id(1L)
                .title("Test Achievement")
                .description("Test Description")
                .build();
    }

    @Test
    @DisplayName("testPublishAchievementEventSuccess")
    void testPublishAchievementEventSuccess() {
        long userId = 1L;
        long achievementId = 1L;

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));
        when(userAchievementRepository.save(any(UserAchievement.class))).thenReturn(new UserAchievement());

        assertDoesNotThrow(() -> achievementService.publishAchievementEvent(userId, achievementId));

        verify(userAchievementRepository, times(1)).save(any(UserAchievement.class));
        verify(achievementEventPublisher, times(1)).publish(any(AchievementEvent.class));
    }

    @Test
    @DisplayName("testPublishAchievementEventAchievementNotFound")
    void testPublishAchievementEventAchievementNotFound() {
        long userId = 1L;
        long achievementId = 1L;

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                achievementService.publishAchievementEvent(userId, achievementId));

        assertEquals("Achievement 1 not found", exception.getMessage());
        verify(userAchievementRepository, never()).save(any(UserAchievement.class));
        verify(achievementEventPublisher, never()).publish(any(AchievementEvent.class));
    }

    @Test
    @DisplayName("testPublishAchievementEventAchievementAlreadyExists")
    void testPublishAchievementEventAchievementAlreadyExists() {
        long userId = 1L;
        long achievementId = 1L;

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));
        doThrow(new AchievementAlreadyExistsException("User 1 already has achievement 1"))
                .when(userAchievementRepository).save(any(UserAchievement.class));

        AchievementAlreadyExistsException exception = assertThrows(AchievementAlreadyExistsException.class, () ->
                achievementService.publishAchievementEvent(userId, achievementId));

    }
}