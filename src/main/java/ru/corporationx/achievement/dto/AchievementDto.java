package ru.corporationx.achievement.dto;

import ru.corporationx.achievement.model.Rarity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementDto {
    private Long id;
    private String title;
    private String description;
    private Rarity rarity;
    private long points;
}
