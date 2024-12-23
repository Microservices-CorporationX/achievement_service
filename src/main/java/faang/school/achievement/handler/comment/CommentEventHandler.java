package faang.school.achievement.handler.comment;

import faang.school.achievement.dto.comment.CommentEvent;
import faang.school.achievement.handler.EventHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public abstract class CommentEventHandler implements EventHandler<CommentEvent> {

    @Async("taskExecutor")
    public abstract void handleEvent(CommentEvent event);
}
