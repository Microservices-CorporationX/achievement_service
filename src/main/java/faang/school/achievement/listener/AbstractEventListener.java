package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener, RedisContainerMessageListener {
    private final ObjectMapper objectMapper;
    private final List<EventHandler<T>> handlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message for event - {}", getEventClass().getSimpleName());
        processEvent(message, getEventClass());
    }

    protected abstract Class<T> getEventClass();

    private void processEvent(Message message, Class<T> eventClass) {
        try {
            T event = handleEvent(message, eventClass);
            handlers.forEach(handler -> {
                log.info("Processing event - {}", eventClass.getSimpleName());
                handler.handle(event);
            });
        } catch (RuntimeException e) {
            log.error("Error while processing event: {}", e.getMessage(), e);
            throw e;
        }
    }

    private T handleEvent(Message message, Class<T> eventClass) {
        try {
            return objectMapper.readValue(message.getBody(), eventClass);
        } catch (IOException e) {
            log.error("Failed to deserialize message to event of type: {}", eventClass.getSimpleName(), e);
            throw new RuntimeException("Deserialization error", e);
        }
    }
}
