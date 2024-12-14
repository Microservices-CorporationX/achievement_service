package faang.school.achievement.listener.impl.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.like.LikePostEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LikePostEventListener extends AbstractEventListener<LikePostEvent> {

    @Value("${spring.data.redis.channel.like-post}")
    private String likePostNotification;

    public LikePostEventListener(ObjectMapper objectMapper,
                                 List<EventHandler<LikePostEvent>> eventHandlers) {
        super(objectMapper, eventHandlers);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, LikePostEvent.class);
    }

    @Override
    public ChannelTopic getChannelTopic() {
        return new ChannelTopic(likePostNotification);
    }
}
