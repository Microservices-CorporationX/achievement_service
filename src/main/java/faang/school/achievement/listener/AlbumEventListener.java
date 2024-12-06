package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.model.AlbumCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AlbumEventListener implements MessageListener, RedisListener {

    private final ObjectMapper objectMapper;
    private final List<EventHandler> handlers;

    @Value("${spring.data.redis.channel.album}")
    private String channelName;

    public void onMessage(Message message, byte[] pattern) {
        try {
            AlbumCreatedEvent event = objectMapper.readValue(message.getBody(), AlbumCreatedEvent.class);
            handlers.stream()
                    .filter(eventHandler -> eventHandler.requiredEvent().equals(AlbumCreatedEvent.class))
                    .forEach(eventHandler -> eventHandler.handleEvent(event.getUserId()));
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid message format", e);
        }
    }

    public MessageListenerAdapter getAdapter() {
        return new MessageListenerAdapter(this);
    }

    public Topic getTopic() {
        return new ChannelTopic(channelName);
    }
}