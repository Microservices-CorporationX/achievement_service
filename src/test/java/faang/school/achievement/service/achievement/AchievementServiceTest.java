package faang.school.achievement.service.achievement;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.AchievementException;
import faang.school.achievement.mapper.AchievementMapperImpl;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    @InjectMocks
    private AchievementService achievementService;

    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Spy
    private AchievementMapperImpl mapper;

    @Test
    public void testGetByUserId() {
        Long userId = 1L;
        Achievement achievement1 = Achievement.builder()
                .title("title")
                .description("no")
                .rarity(Rarity.LEGENDARY).build();

        Achievement achievement2 = Achievement.builder()
                .title("name")
                .description("description")
                .rarity(Rarity.LEGENDARY).build();

        UserAchievement userAchievement1 = UserAchievement.builder().achievement(achievement1).build();
        UserAchievement userAchievement2 = UserAchievement.builder().achievement(achievement2).build();

        Mockito.when(userAchievementRepository.findByUserId(userId))
                .thenReturn(List.of(userAchievement1, userAchievement2));

        List<AchievementDto> result = achievementService.getByUserId(userId);

        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void testGetNotExisting() {
        Mockito.when(achievementRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(AchievementException.class, () -> achievementService.get(anyLong()));
    }

    @Test
    public void testGetPositive() {
        Achievement achievement1 = Achievement.builder()
                .title("title")
                .description("no")
                .rarity(Rarity.LEGENDARY).build();

        Mockito.when(achievementRepository.findById(anyLong())).thenReturn(Optional.of(achievement1));

        achievementService.get(anyLong());

        verify(mapper).toDto(achievement1);
    }

    @Test
    public void testGetByUserIdUnearned() {
        Long userId = 1L;
        Achievement achievement1 = Achievement.builder()
                .title("title")
                .description("no")
                .rarity(Rarity.LEGENDARY).build();

        Achievement achievement2 = Achievement.builder()
                .title("name")
                .description("description")
                .rarity(Rarity.LEGENDARY).build();

        AchievementProgress progress1 = AchievementProgress.builder().achievement(achievement1).build();
        AchievementProgress progress2 = AchievementProgress.builder().achievement(achievement2).build();

        Mockito.when(achievementProgressRepository.findByUserId(userId))
                .thenReturn(List.of(progress1, progress2));

        List<AchievementDto> result = achievementService.getByUserIdUnearned(userId);

        Assertions.assertEquals(2, result.size());
    }
}
