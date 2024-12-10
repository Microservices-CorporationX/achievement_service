package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.mapper.achievement.AchievementMapper;
import faang.school.achievement.model.Achievement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private AchievementCache achievementCache;

    @Spy
    private AchievementMapper achievementMapper = Mappers.getMapper(AchievementMapper.class);

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

}