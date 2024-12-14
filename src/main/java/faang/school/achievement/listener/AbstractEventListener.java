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

    public void processEvent(Message message, Class<T> eventType) {
        try {
            log.info("Received message for event - {}", eventType.getSimpleName());
            T event = objectMapper.readValue(message.getBody(), eventType);
            handlers.forEach(handler -> handler.handleEvent(event));
        } catch (IOException e) {
            String exceptionMessage = String.format("Unable to parse event: %s, with message: %s",
                    eventType.getName(), message);
            log.error(exceptionMessage, e);
            throw new RuntimeException(e);
        }
    }
}
