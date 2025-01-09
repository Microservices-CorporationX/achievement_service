package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.MentorshipAcceptedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class MentorshipAcceptedEventListener implements MessageListener {
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipAcceptedEvent event = objectMapper.readValue(message.getBody(), MentorshipAcceptedEvent.class);
        } catch (IOException e) {
            handleDeserializationError(message, e);
        }
    }

    private void handleDeserializationError(Message message, IOException e) {
        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
        log.error("Error while serializing {} from redis. Error: {}", messageBody, e.getMessage(), e);
    }
}
