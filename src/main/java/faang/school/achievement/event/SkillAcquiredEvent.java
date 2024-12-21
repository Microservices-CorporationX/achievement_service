package faang.school.achievement.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillAcquiredEvent {
    private Long achievementId;
    private Long authorId;
    private Long receiverId;
    private Long skillId;
}
