package faang.school.achievement.listener.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.post.PostEvent;
import faang.school.achievement.handler.post.PostEventHandler;
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
public class PostEventListener implements MessageListener {

    private final List<PostEventHandler> handlers;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            PostEvent event = objectMapper.readValue(message.getBody(), PostEvent.class);
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
