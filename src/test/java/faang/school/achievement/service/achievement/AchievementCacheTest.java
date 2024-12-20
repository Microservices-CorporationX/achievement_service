package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementCacheTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private AchievementRedisService redisService;

    @InjectMocks
    private AchievementCache achievementCache;

    @Test
    void fillCacheTest() {
        Achievement achievementFirst = new Achievement();
        achievementFirst.setTitle("Achievement1");
        Achievement achievementSecond = new Achievement();
        achievementSecond.setTitle("Achievement2");
        when(achievementRepository.findAll()).thenReturn(List.of(achievementFirst, achievementSecond));
        when(redisService.getAchievement("Achievements", "Achievement1")).thenReturn(achievementFirst);
        when(redisService.getAchievement("Achievements", "Achievement2")).thenReturn(achievementSecond);

        achievementCache.fillCache();

        verify(achievementRepository, times(1)).findAll();
        Achievement result1 = achievementCache.get("Achievement1");
        Achievement result2 = achievementCache.get("Achievement2");
        assertEquals(achievementFirst.getTitle(), result1.getTitle());
        assertEquals(achievementSecond.getTitle(), result2.getTitle());
    }

    @Test
    void getAllTest() {
        Achievement achievementFirst = new Achievement();
        achievementFirst.setTitle("Achievement1");
        Achievement achievementSecond = new Achievement();
        achievementSecond.setTitle("Achievement2");
        when(redisService.getAllAchievements("Achievements")).thenReturn(List.of(achievementFirst, achievementSecond));

        List<Achievement> result = achievementCache.getAll();

        assertEquals(2, result.size());
        assertEquals(achievementFirst.getTitle(), result.get(0).getTitle());
        assertEquals(achievementSecond.getTitle(), result.get(1).getTitle());
    }

}