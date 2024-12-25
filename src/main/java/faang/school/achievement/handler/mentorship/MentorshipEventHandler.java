package faang.school.achievement.handler.mentorship;

import faang.school.achievement.dto.mentorship.MentorshipStartEvent;
import faang.school.achievement.handler.EventHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public abstract class MentorshipEventHandler implements EventHandler<MentorshipStartEvent> {

    @Async("taskExecutor")
    public abstract void handleEvent(MentorshipStartEvent event);
}
