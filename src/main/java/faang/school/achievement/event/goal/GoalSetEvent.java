package faang.school.achievement.event.goal;

import faang.school.achievement.event.Event;
import lombok.Builder;

@Builder
public record GoalSetEvent(Long userId, Long goalId) implements Event{
}
