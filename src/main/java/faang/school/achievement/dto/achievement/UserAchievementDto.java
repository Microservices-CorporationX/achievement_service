package faang.school.achievement.dto.achievement;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserAchievementDto(
    @NotNull Long id,
    @NotNull long achievementId,
    @NotNull long userId
) {
}
