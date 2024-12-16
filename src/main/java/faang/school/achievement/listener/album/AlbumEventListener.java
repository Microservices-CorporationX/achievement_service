package faang.school.achievement.listener.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.album.AlbumCreatedEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.service.AsyncHandlerService;
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
public class AlbumEventListener implements MessageListener {

    private final List<EventHandler<AlbumCreatedEvent>> handlers;
    private final ObjectMapper objectMapper;
    private final AsyncHandlerService<AlbumCreatedEvent> asyncHandlerService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            AlbumCreatedEvent event = objectMapper.readValue(message.getBody(), AlbumCreatedEvent.class);
            handlers.forEach(handler -> {
                log.info("Handling event {} with handler: {}", event, handler.getClass().getSimpleName());
                asyncHandlerService.handle(handler, event);
            });
        } catch (IOException e) {
            log.error("Error reading value");
            throw new RuntimeException(e);
        }
    }
}
