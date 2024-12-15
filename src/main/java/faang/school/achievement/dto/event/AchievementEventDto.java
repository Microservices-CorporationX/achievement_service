package faang.school.achievement.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementEventDto {
    private Long userId;
    private String achievementTitle;
    private Long points;
    private Long projectId;
    private Long teamId;
}
