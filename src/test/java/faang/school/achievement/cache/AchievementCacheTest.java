package faang.school.achievement.cache;

import static org.junit.jupiter.api.Assertions.*;

import faang.school.achievement.mapper.AchievementMapperImpl;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class AchievementCacheTest {

    @Spy
    private AchievementMapperImpl achievementMapper;

    @Mock
    private AchievementRepository achievementRepository;

    @InjectMocks
    private AchievementCache cache;

    @Test
    public void testSetCache() {

        Achievement creator = Achievement.builder().title("creator").description("create first project").build();
        Achievement copywriter = Achievement.builder().title("copywriter").description("type two descriptions").build();
        Iterable<Achievement> achievements = Arrays.asList(creator, copywriter);

        Mockito.when(achievementRepository.findAll()).thenReturn(achievements);

        cache.setCache();

        assertEquals(achievementMapper.toDto(creator), cache.get(creator.getTitle()));
        assertEquals(achievementMapper.toDto(copywriter), cache.get(copywriter.getTitle()));
        assertNull(cache.get("Not existing achievement"));
    }
}
