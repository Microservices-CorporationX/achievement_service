package faang.school.achievement.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementEvent {
    private long userId;
    private String title;
    private LocalDateTime timestamp;
}
