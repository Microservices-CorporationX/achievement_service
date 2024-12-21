package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementCacheDto;
import faang.school.achievement.model.Achievement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AchievementMapper {

    AchievementCacheDto toDto(Achievement achievement);

    Achievement toEntity(AchievementCacheDto dto);
}
