package faang.school.achievement.listener.team;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.team.TeamEventHandler;
import faang.school.achievement.dto.team.TeamEvent;
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
public class TeamEventListener implements MessageListener {

    private final List<TeamEventHandler> handlers;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            TeamEvent event = objectMapper.readValue(message.getBody(), TeamEvent.class);
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
