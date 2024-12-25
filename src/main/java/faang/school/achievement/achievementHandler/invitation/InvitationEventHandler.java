package faang.school.achievement.achievementHandler.invitation;

import faang.school.achievement.achievementHandler.EventHandler;
import faang.school.achievement.dto.invitation.InviteSentEvent;
import org.springframework.scheduling.annotation.Async;

public abstract class InvitationEventHandler implements EventHandler<InviteSentEvent> {

    @Async("InvitationEventExecutor")
    public abstract void handleEvent(InviteSentEvent event);
}
