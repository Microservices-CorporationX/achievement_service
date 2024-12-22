package faang.school.achievement.service.achievement;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.AchievementRedisService;
import faang.school.achievement.service.RedisAchievementCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisAchievementCacheTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private AchievementRedisService redisService;

    @InjectMocks
    private RedisAchievementCache achievementCache;

    @Test
    void getAllTest() {
        Achievement achievementFirst = new Achievement();
        achievementFirst.setTitle("Achievement1");
        Achievement achievementSecond = new Achievement();
        achievementSecond.setTitle("Achievement2");
        when(redisService.getAllAchievements()).thenReturn(List.of(achievementFirst, achievementSecond));

        List<Achievement> result = achievementCache.getAll();

        assertEquals(2, result.size());
        assertEquals(achievementFirst.getTitle(), result.get(0).getTitle());
        assertEquals(achievementSecond.getTitle(), result.get(1).getTitle());
    }
}