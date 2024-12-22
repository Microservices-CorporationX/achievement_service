package faang.school.achievement.dto.achievement;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AchievementProgressDto(
    @NotNull Long id,
    @NotNull long achievementId,
    @NotNull long userId,
    @NotNull long currentPoints
) {
}
