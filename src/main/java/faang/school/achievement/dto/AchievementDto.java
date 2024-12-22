package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;
import lombok.Builder;

@Builder
public record AchievementDto(
        long id,
        String title,
        Rarity rarity
) {
}
