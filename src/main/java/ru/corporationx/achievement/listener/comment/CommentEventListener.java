package ru.corporationx.achievement.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.corporationx.achievement.dto.comment.CommentEvent;
import ru.corporationx.achievement.handler.comment.CommentEventHandler;
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
public class CommentEventListener implements MessageListener {

    private final List<CommentEventHandler> handlers;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            CommentEvent event = objectMapper.readValue(message.getBody(), CommentEvent.class);
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
