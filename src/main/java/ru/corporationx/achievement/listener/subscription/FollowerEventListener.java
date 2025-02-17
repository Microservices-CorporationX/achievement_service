package ru.corporationx.achievement.listener.subscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.corporationx.achievement.dto.subscription.FollowerEvent;
import ru.corporationx.achievement.handler.subscription.FollowerEventHandler;
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
public class FollowerEventListener implements MessageListener {

    private final List<FollowerEventHandler> handlers;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            handlers.forEach(eventHandler -> {
                log.debug("Handling event with handler: {}", eventHandler.getClass().getSimpleName());
                eventHandler.handleEvent(event);
            });
        } catch (IOException e) {
            log.error("Error reading value from {}", message.getChannel());
            throw new RuntimeException("Failed to read from message body", e);
        }
    }
}
