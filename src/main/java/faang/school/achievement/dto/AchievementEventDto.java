package faang.school.achievement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AchievementEventDto {
    private Long userId;
    private String achievement;
}
