package faang.school.achievement.mapper.achievement;

import faang.school.achievement.dto.achievement.AchievementProgressDto;
import faang.school.achievement.model.AchievementProgress;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AchievementProgressMapper {
    AchievementProgressDto toDto(AchievementProgress achievementProgress);
}
