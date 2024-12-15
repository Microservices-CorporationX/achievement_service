package faang.school.achievement.mapper.achievement;

import faang.school.achievement.dto.achievement.AchievementDto;
import faang.school.achievement.model.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AchievementMapper {
    AchievementDto toDto(Achievement achievement);
}
