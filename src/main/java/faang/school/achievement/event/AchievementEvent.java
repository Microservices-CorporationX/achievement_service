package faang.school.achievement.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AchievementEvent {

    private Long userId;
    private String title;
    private String description;
}
