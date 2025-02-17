package ru.corporationx.achievement.listener.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.corporationx.achievement.dto.project.ProjectEvent;
import ru.corporationx.achievement.handler.project.ProjectEventHandler;
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
public class ProjectEventListener implements MessageListener {
    private final List<ProjectEventHandler> handlers;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ProjectEvent event = objectMapper.readValue(message.getBody(), ProjectEvent.class);
            handlers.forEach(projectEventHandler -> {
                log.debug("Handling event with handler: {}", projectEventHandler.getClass().getSimpleName());
                projectEventHandler.handleEvent(event);
            });
        } catch (IOException e) {
            log.error("Error reading value from {}", message.getChannel());
            throw new RuntimeException(e);
        }
    }
}
