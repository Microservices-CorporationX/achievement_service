package faang.school.achievement.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.CommentEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> {

    @Value("${spring.data.redis.channel.comment}")
    private String commentChannel;

    public CommentEventListener(
            ObjectMapper objectMapper,
            List<EventHandler<CommentEvent>> abstractEventHandlers
    ) {
        super(objectMapper, abstractEventHandlers);
    }

    @Override
    public MessageListenerAdapter getAdapter() {
        return new MessageListenerAdapter(this);
    }

    @Override
    public Topic getTopic() {
        return new ChannelTopic(commentChannel);
    }

    @Override
    public Class<CommentEvent> getEventClass() {
        return CommentEvent.class;
    }
}
