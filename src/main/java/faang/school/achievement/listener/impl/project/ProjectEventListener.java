package faang.school.achievement.listener.impl.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.project.ProjectEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProjectEventListener extends AbstractEventListener<ProjectEvent> {

    @Value("${spring.data.redis.channel.project}")
    private String projectChannel;

    public ProjectEventListener(ObjectMapper objectMapper,
                                List<EventHandler<ProjectEvent>> eventHandlers) {
        super(objectMapper, eventHandlers);
    }

    @Override
    public ChannelTopic getChannelTopic() {
        return new ChannelTopic(projectChannel);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, ProjectEvent.class);
    }
}