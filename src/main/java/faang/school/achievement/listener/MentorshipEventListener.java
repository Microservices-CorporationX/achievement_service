package faang.school.achievement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.MentorshipStartEvent;
import faang.school.achievement.handler.mentorship.MentorshipHandler;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MentorshipEventListener implements MessageListener {
    private final List<MentorshipHandler> mentorshipHandlers;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String stringJson = new String(message.getBody());
        MentorshipStartEvent startEvent = mapJsonToEvent(stringJson);

        for (MentorshipHandler handler : mentorshipHandlers) {
            log.info("calling startHandling for all MentorshipHandler");
            handler.startHandling(startEvent);
        }
    }

    public MentorshipStartEvent mapJsonToEvent(String stringJson) {
        MentorshipStartEvent startEvent = null;
        try {
            log.info("map jsonString to startEvent");
            startEvent = objectMapper.readValue(stringJson, MentorshipStartEvent.class);
            return startEvent;
        } catch (JsonProcessingException e) {
            log.error("invalid json message", e);
            throw new IllegalArgumentException("invalid json message", e);
        }
    }
}
