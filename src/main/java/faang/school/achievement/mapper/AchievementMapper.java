package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AchievementMapper {

    AchievementDto toAchievementDto(Achievement achievement);

    List<AchievementDto> toAchievementDto(List<Achievement> achievements);

    @Mapping(target = "achievementId", source = "achievement.id")
    @Mapping(target = "title", source = "achievement.title")
    @Mapping(target = "points", source = "achievement.points")
    @Mapping(target = "rarity", source = "achievement.rarity")
    AchievementProgressDto toAchievementProgressDto(AchievementProgress achievementProgress);

    List<AchievementProgressDto> toAchievementProgressDto(List<AchievementProgress> achievementProgresses);
}
