package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.SkillAcquiredEvent;
import faang.school.achievement.handler.EventHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SkillEventListener extends AbstractListener<SkillAcquiredEvent> {
    public SkillEventListener(ObjectMapper objectMapper, List<EventHandler<SkillAcquiredEvent>> eventHandlers) {
        super(objectMapper, eventHandlers);
    }

    @Override
    protected SkillAcquiredEvent listenEvent(Message message) throws IOException {
        return objectMapper.readValue(message.getBody(), SkillAcquiredEvent.class);
    }
}
