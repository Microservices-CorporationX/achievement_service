package faang.school.achievement.dto.redisevent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementEvent {
    private long userId;
    private String achievement;
}
