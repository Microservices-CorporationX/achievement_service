package faang.school.achievement.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.CommentEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> {

    @Value("${spring.data.redis.channel.comment}")
    private String commentChannel;

    public CommentEventListener(ObjectMapper objectMapper,
                                List<EventHandler<CommentEvent>> eventHandlers) {
        super(objectMapper, eventHandlers);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, CommentEvent.class);
    }

    @Override
    public ChannelTopic getChannelTopic() {
        return new ChannelTopic(commentChannel);
    }
}
