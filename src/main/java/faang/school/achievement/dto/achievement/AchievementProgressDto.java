package faang.school.achievement.dto.achievement;

import lombok.Builder;

@Builder
public record AchievementProgressDto(
    Long id,
    long achievementId,
    long userId,
    long currentPoints
) {
}
