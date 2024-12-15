package faang.school.achievement.dto.achievement;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AchievementDto {
    private Long id;
    private String title;
    private String description;
    private long points;
}
