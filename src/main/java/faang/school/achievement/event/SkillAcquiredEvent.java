package faang.school.achievement.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillAcquiredEvent {
    private long recommenderId;
    private long recipientId;
    private long skillId;
}
