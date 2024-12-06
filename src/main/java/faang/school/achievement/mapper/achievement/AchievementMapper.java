package faang.school.achievement.mapper.achievement;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.model.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AchievementMapper {
    AchievementDto toDto(Achievement achievement);

    List<AchievementDto> toDtoList(List<Achievement> events);
}
