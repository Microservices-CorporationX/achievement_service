package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;

import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.handler.EventHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProfilePicEventListener implements MessageListener {

    private final List<EventHandler> handlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody());
            ProfilePicEvent event = new ObjectMapper().readValue(json, ProfilePicEvent.class);
            log.info("Received event: {}", event);

            handlers.forEach(handler -> handler.handleEvent(event));
        } catch (Exception e) {
            log.error("Failed to process event", e);
        }
    }
}