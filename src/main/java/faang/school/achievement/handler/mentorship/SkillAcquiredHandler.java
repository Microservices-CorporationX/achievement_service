package faang.school.achievement.handler.mentorship;

import faang.school.achievement.event.SkillAcquiredEvent;

public interface SkillAcquiredHandler {
    void handleEvent(SkillAcquiredEvent event);
}
