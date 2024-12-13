package faang.school.achievement.handler.mentorship;

import faang.school.achievement.event.MentorshipStartEvent;

public interface MentorshipHandler {
    void startHandling(MentorshipStartEvent mentorshipStartEvent);
}
