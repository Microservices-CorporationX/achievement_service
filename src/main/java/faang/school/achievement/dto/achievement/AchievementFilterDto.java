package faang.school.achievement.dto.achievement;

import faang.school.achievement.model.Rarity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AchievementFilterDto(
    @NotNull String title,
    @NotNull String description,
    @NotNull Rarity rarity
) {
}
