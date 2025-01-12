package faang.school.achievement.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.exceptions.MessageMappingException;
import faang.school.achievement.message.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T extends EventHandler<V>, V> implements MessageListener {

    private final List<T> eventHandlers;
    private final ObjectMapper objectMapper;

    @Retryable(maxAttempts = 5, backoff = @Backoff(multiplier = 2.0))
    public void handleEvent(Message message, Class<V> eventClass) {
        try {
            V event = objectMapper.readValue(message.getBody(), eventClass);
            eventHandlers.forEach(eventHandler -> eventHandler.handleEvent(event));
        } catch (IOException e) {
            log.error("Failed to map message to {}", eventClass.getName());
            throw new MessageMappingException("Failed to convert message to " + eventClass.getName());
        }
    }
}
