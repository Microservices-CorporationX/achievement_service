package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.MentorshipStartEvent;

import faang.school.achievement.handler.mentorship.MentorshipStartEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MentorshipStartEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final List<MentorshipStartEventHandler> handlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipStartEvent event = objectMapper.readValue(message.getBody(), MentorshipStartEvent.class);
            handlers.forEach(handler -> handler.handleEvent(event));
        } catch (IOException e) {
            log.error("Error when deserializing a message", e);
        }
    }
}