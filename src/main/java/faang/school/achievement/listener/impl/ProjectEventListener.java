package faang.school.achievement.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.project.ProjectEvent;
import faang.school.achievement.event_hendler.impl.project.ProjectEventHandler;
import faang.school.achievement.listener.RedisContainerMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectEventListener implements MessageListener, RedisContainerMessageListener {

    @Value("${spring.data.redis.channel.project}")
    private String projectChannel;

    private final ObjectMapper objectMapper;
    private final List<ProjectEventHandler> eventHandlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProjectEvent event = objectMapper.convertValue(message.getBody(), ProjectEvent.class);
        eventHandlers.forEach(handler -> handler.handleEvent(event));
    }

    @Override
    public ChannelTopic getChannelTopic() {
        return new ChannelTopic(projectChannel);
    }
}
