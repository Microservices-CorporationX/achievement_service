package faang.school.achievement.handler.mentorship;

import faang.school.achievement.event.MentorshipAcceptedEvent;

public interface MentorshipHandler {
    void startHandling(MentorshipAcceptedEvent mentorshipAcceptedEvent);
}
