package faang.school.achievement.cachetests;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AchievementCacheTest {

    private AchievementRepository achievementRepository;
    private AchievementCache achievementCache;

    @BeforeEach
    void setUp() {
        achievementRepository = mock(AchievementRepository.class);

        Achievement achievement1 = Achievement.builder()
                .id(1L)
                .title("Achievement 1")
                .description("Description 1")
                .rarity(Rarity.COMMON)
                .userAchievements(Collections.emptyList())
                .progresses(Collections.emptyList())
                .points(10)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Achievement achievement2 = Achievement.builder()
                .id(2L)
                .title("Achievement 2")
                .description("Description 2")
                .rarity(Rarity.RARE)
                .userAchievements(Collections.emptyList())
                .progresses(Collections.emptyList())
                .points(20)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(achievementRepository.findAll()).thenReturn(List.of(achievement1, achievement2));

        achievementCache = new AchievementCache(achievementRepository);
    }

    @Test
    void testGetExistingAchievement() {
        Optional<Achievement> achievement = achievementCache.getAchievement("Achievement 1");
        assertTrue(achievement.isPresent());
        assertEquals("Achievement 1", achievement.get().getTitle());
    }

    @Test
    void testGetNonExistingAchievement() {
        Optional<Achievement> achievement = achievementCache.getAchievement("Non-Existent");
        assertTrue(achievement.isEmpty());
    }

    @Test
    void testCacheInitialization() {
        verify(achievementRepository, times(1)).findAll();
    }
}