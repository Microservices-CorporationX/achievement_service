package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AchievementDto {

    private String title;
    private String description;
    private Rarity rarity;
    private long points;
}
