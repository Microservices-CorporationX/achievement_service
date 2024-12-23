package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.RecommendationEvent;
import faang.school.achievement.exception.MessageProcessingException;
import faang.school.achievement.hander.RecommendationEventHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final List<RecommendationEventHandler> handlers;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            RecommendationEvent event = objectMapper.readValue(message.getBody(), RecommendationEvent.class);
            handlers.forEach(eventHandler -> eventHandler.handleEvent(event));
        } catch (IOException e) {
            log.error("Failed to parse or read message body", e);
            throw new MessageProcessingException("Message parsing failed", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred while processing the message", e);
            throw new MessageProcessingException("Unexpected failure", e);
        }
    }
}
