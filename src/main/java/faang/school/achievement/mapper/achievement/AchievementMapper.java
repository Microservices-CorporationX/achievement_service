package faang.school.achievement.mapper.achievement;

import faang.school.achievement.dto.user.achievement.AchievementDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AchievementMapper {

    @Mapping(target = "userAchievements", ignore = true)
    @Mapping(target = "progresses", ignore = true)
    Achievement toEntity(AchievementDto achievementDto);

    @Mapping(source = "userAchievements", target = "userAchievementsIds", qualifiedByName = "UserAchievement")
    @Mapping(source = "progresses", target = "progressesIds", qualifiedByName = "AchievementProgress")
    AchievementDto toDto(Achievement achievement);

    @Named("UserAchievement")
    default List<Long> getUserAchievements(List<UserAchievement> achievements) {
        if (achievements == null) {
            return new ArrayList<>();
        }
        return achievements.stream()
                .map(UserAchievement::getId)
                .collect(Collectors.toList());
    }

    @Named("AchievementProgress")
    default List<Long> getAchievementProgress(List<AchievementProgress> achievements) {
        if (achievements == null) {
            return new ArrayList<>();
        }
        return achievements.stream()
                .map(AchievementProgress::getId)
                .collect(Collectors.toList());
    }

}
