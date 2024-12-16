package faang.school.achievement.dto.achievement;

import lombok.Builder;

@Builder
public record UserAchievementDto(
    Long id,
    long achievementId,
    long userId
) {
}
