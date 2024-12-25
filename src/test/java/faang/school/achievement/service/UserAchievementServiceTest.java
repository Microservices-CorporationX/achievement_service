package faang.school.achievement.service;

import faang.school.achievement.exception.AchievementAlreadyExistsException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAchievementServiceTest {
   @Mock
   private UserAchievementRepository userAchievementRepository;

   @InjectMocks
   private UserAchievementService userAchievementService;

   private Achievement achievement;
   private final long userId = 1L;;

   @BeforeEach
   void setUp() {
     achievement = Achievement.builder()
              .id(1L)
              .title("Test Achievement")
              .description("This is a test achievement")
              .rarity(Rarity.COMMON)
              .build();
   }

   @Test
    void testCreateUserAchievementSuccess() {
       UserAchievement userAchievement = UserAchievement.builder()
               .userId(userId)
               .achievement(achievement)
               .build();

       when(userAchievementRepository.save(userAchievement)).thenReturn(new UserAchievement());

       userAchievementService.createUserAchievement(userId, achievement.getId(), achievement);

       verify(userAchievementRepository, times(1)).save(any(UserAchievement.class));
   }

   @Test
   void testCreateUserAchievementThrowsException() {
      when(userAchievementRepository.save(any(UserAchievement.class)))
              .thenThrow(new DataIntegrityViolationException("Duplicate entry"));

     assertThrows(AchievementAlreadyExistsException.class, () -> {
         userAchievementService.createUserAchievement(userId, achievement.getId(), achievement);
      });

      verify(userAchievementRepository, times(1)).save(any(UserAchievement.class));
   }

   @Test
   void testCreateUserAchievementThrowsEntityNotFoundException() {
      when(userAchievementRepository.save(any(UserAchievement.class)))
              .thenThrow(new EntityNotFoundException("User not found"));

      assertThrows(EntityNotFoundException.class, () -> {
         userAchievementService.createUserAchievement(userId, achievement.getId(), achievement);
      });

      verify(userAchievementRepository, times(1)).save(any(UserAchievement.class));
   }

   @Test
   void testCreateUserAchievementThrowsUnexpectedException() {
      when(userAchievementRepository.save(any(UserAchievement.class)))
              .thenThrow(new RuntimeException("Unexpected error"));

      assertThrows(RuntimeException.class, () -> {
         userAchievementService.createUserAchievement(userId, achievement.getId(), achievement);
      });

      verify(userAchievementRepository, times(1)).save(any(UserAchievement.class));
   }
}
