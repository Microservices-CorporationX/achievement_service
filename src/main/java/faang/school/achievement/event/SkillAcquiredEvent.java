package faang.school.achievement.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SkillAcquiredEvent {
    private long userId;
    private long skillId;
    private LocalDateTime skillAcquiredDateTime;

    public SkillAcquiredEvent(long userId, long skillId) {
        this.userId = userId;
        this.skillId = skillId;
        skillAcquiredDateTime = LocalDateTime.now();
    }
}
