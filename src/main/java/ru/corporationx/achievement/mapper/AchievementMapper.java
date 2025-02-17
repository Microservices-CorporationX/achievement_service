package ru.corporationx.achievement.mapper;

import ru.corporationx.achievement.dto.AchievementDto;
import ru.corporationx.achievement.model.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AchievementMapper {

    AchievementDto toDto(Achievement achievement);
    Achievement toEntity(AchievementDto achievementDto);

    List<AchievementDto> toDto(List<Achievement> achievement);
}
