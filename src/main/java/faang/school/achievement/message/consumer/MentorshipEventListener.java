package faang.school.achievement.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.message.handler.mentorship.MentorshipEventHandler;
import faang.school.achievement.message.event.MentorshipEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MentorshipEventListener extends AbstractEventListener<MentorshipEventHandler, MentorshipEvent> {

    public MentorshipEventListener(List<MentorshipEventHandler> eventHandlers, ObjectMapper objectMapper) {
        super(eventHandlers, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received MentorshipEvent");
        handleEvent(message, MentorshipEvent.class);
    }
}
