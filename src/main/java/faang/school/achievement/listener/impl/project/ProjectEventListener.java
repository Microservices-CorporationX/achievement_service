package faang.school.achievement.listener.impl.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.project.ProjectEvent;
import faang.school.achievement.event_handler.impl.project.ProjectAchievementHandler;
import faang.school.achievement.listener.RedisContainerMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectEventListener implements MessageListener, RedisContainerMessageListener {

    @Value("${spring.data.redis.channel.project}")
    private String projectChannel;

    private final ObjectMapper objectMapper;
    private final List<ProjectAchievementHandler> eventHandlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ProjectEvent event = objectMapper.readValue(message.getBody(), ProjectEvent.class);
            eventHandlers.forEach(handler -> handler.handleEvent(event));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ChannelTopic getChannelTopic() {
        return new ChannelTopic(projectChannel);
    }
}
