package faang.school.achievement.dto.achievement;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AchievementProgressDto {
    private Long id;
    private Long userId;
    private String title;
    private long currentPoints;
}
