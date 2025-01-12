package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;
import lombok.Builder;

@Builder
public record AchievementProgressDto (
        long id,
        long achievementId,
        String title,
        Rarity rarity,
        long currentPoints,
        long points
) {
}
