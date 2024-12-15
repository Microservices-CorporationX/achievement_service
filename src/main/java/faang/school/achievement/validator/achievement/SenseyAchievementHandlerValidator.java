package faang.school.achievement.validator.achievement;

import faang.school.achievement.event.MentorshipAcceptedEvent;
import org.springframework.stereotype.Component;

@Component
public class SenseyAchievementHandlerValidator {
    public void validateMentorshipStartEvent(MentorshipAcceptedEvent mentorshipAcceptedEvent) {
        if (mentorshipAcceptedEvent == null || mentorshipAcceptedEvent.getRequesterUserId() < 0 || mentorshipAcceptedEvent.getRequesterUserId() < 0 || mentorshipAcceptedEvent.getUserContextId() < 0) {
            throw new IllegalArgumentException("Invalid mentorshipStartEvent object");
        }
    }
}
