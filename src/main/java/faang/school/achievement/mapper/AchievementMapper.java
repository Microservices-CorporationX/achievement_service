package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementCacheDto;
import faang.school.achievement.model.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AchievementMapper {

    AchievementCacheDto toDto(Achievement achievement);

    @Mapping(target = " userAchievements", ignore = true)
    @Mapping(target = "progresses", ignore = true)
    Achievement toEntity(AchievementCacheDto dto);
}
