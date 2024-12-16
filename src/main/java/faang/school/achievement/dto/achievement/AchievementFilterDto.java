package faang.school.achievement.dto.achievement;

import faang.school.achievement.model.Rarity;
import lombok.Builder;

@Builder
public record AchievementFilterDto(
    String title,
    String description,
    Rarity rarity
) {
}
