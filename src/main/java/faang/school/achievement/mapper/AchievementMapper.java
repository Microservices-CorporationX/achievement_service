package faang.school.achievement.mapper;

import faang.school.achievement.handler.AchievementDto;
import faang.school.achievement.model.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AchievementMapper {

    AchievementDto toDto(Achievement achievement);
    Achievement toEntity(AchievementDto achievementDto);

    List<AchievementDto> toDto(List<Achievement> achievement);
}
