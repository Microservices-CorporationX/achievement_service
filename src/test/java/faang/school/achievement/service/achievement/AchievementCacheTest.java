package faang.school.achievement.service.achievement;

import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.AchievementCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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

    @Spy
    private AchievementMapper achievementMapper = Mappers.getMapper(AchievementMapper.class);

    @InjectMocks
    private faang.school.achievement.service.achievement.AchievementCache achievementCache;


    @Test
    void fillCacheTest() {
        Achievement achievementFirst = new Achievement();
        achievementFirst.setTitle("Achievement1");
        Achievement achievementSecond = new Achievement();
        achievementSecond.setTitle("Achievement2");
        when(achievementRepository.findAll()).thenReturn(List.of(achievementFirst, achievementSecond));

        achievementCache.fillCache();

        verify(achievementRepository, times(1)).findAll();
        Achievement result1 = achievementCache.get("Achievement1");
        Achievement result2 = achievementCache.get("Achievement2");
        assertEquals("Achievement1", result1.getTitle());
        assertEquals("Achievement2", result2.getTitle());
    }

    @Test
    void getInDBTest() {
        Achievement achievementFirst = new Achievement();
        achievementFirst.setTitle("Achievement1");
        Achievement achievementSecond = new Achievement();
        achievementSecond.setTitle("Achievement2");
        when(achievementRepository.findAll()).thenReturn(List.of(achievementFirst));
        achievementCache.fillCache();

        when(achievementRepository.findAll()).thenReturn(List.of(achievementFirst, achievementSecond));
        achievementCache.get(achievementSecond.getTitle());

        List<Achievement> result = achievementCache.getAll();
        assertEquals(2, result.size());
        assertEquals("Achievement1", result.get(0).getTitle());
        assertEquals("Achievement2", result.get(1).getTitle());
    }

    @Test
    void getAllTest() {
        Achievement achievementFirst = new Achievement();
        achievementFirst.setTitle("Achievement1");
        Achievement achievementSecond = new Achievement();
        achievementSecond.setTitle("Achievement2");

        when(achievementRepository.findAll()).thenReturn(List.of(achievementFirst, achievementSecond));
        achievementCache.fillCache();

        List<Achievement> result = achievementCache.getAll();

        assertEquals(2, result.size());
        assertEquals("Achievement1", result.get(0).getTitle());
        assertEquals("Achievement2", result.get(1).getTitle());
    }
}
