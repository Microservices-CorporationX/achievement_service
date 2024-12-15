package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementCacheDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AchievementMapperTest {

    private AchievementMapper achievementMapper;
    private Achievement achievement;
    private AchievementCacheDto dto;

    @BeforeEach
    public void setUp() {
        achievementMapper = Mappers.getMapper(AchievementMapper.class);
        achievement = Achievement.builder()
                .id(1L)
                .title("title")
                .description("description")
                .rarity(Rarity.EPIC)
                .userAchievements(null)
                .progresses(null)
                .points(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        dto = AchievementCacheDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .rarity(Rarity.EPIC)
                .points(1L)
                .createdAt(achievement.getCreatedAt())
                .updatedAt(achievement.getUpdatedAt())
                .build();
    }

    @Test
    @DisplayName("Test map entity to dto success")
    void testMapEntityToDtoSuccess() {
        AchievementCacheDto result = achievementMapper.toDto(achievement);
        assertEquals(dto, result);
    }

    @Test
    @DisplayName("Test map dto to entity success")
    void testMapDtoToEntitySuccess() {
        Achievement result = achievementMapper.toEntity(dto);
        assertEquals(achievement, result);
    }
}