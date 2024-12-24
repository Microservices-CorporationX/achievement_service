package faang.school.achievement.listener.goal;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.goal.GoalSetEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalEventListener extends AbstractEventListener<GoalSetEvent> {
    public GoalEventListener(ObjectMapper objectMapper, List<EventHandler> handlerList) {
        super(objectMapper, handlerList);
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        handleEvent(message, GoalSetEvent.class, event -> handlerList.stream()
                .filter(handler -> handler.isApplicable(event))
                .forEach(eventHandler -> eventHandler.handle(event)));
    }
}
