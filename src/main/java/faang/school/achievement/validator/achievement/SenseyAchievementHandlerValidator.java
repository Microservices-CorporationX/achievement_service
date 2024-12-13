package faang.school.achievement.validator.achievement;

import faang.school.achievement.event.MentorshipStartEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class SenseyAchievementHandlerValidator {
    public void validateMentorshipStartEvent(MentorshipStartEvent mentorshipStartEvent) {
        if (mentorshipStartEvent == null || mentorshipStartEvent.getMenteeId() < 0 || mentorshipStartEvent.getMentorId() < 0 || mentorshipStartEvent.getUserContextId() < 0) {
            throw new IllegalArgumentException("Invalid mentorshipStartEvent object");
        }
    }
}
