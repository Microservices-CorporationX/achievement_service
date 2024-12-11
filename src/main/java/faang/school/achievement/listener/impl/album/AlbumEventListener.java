package faang.school.achievement.listener.impl.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.album.AlbumCreatedEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.RedisContainerMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AlbumEventListener implements MessageListener, RedisContainerMessageListener {

    private final ObjectMapper objectMapper;
    private final List<EventHandler<AlbumCreatedEvent>> handlers;

    @Value("${spring.data.redis.channel.album}")
    private String channelName;

    public void onMessage(Message message, byte[] pattern) {
        try {
            AlbumCreatedEvent event = objectMapper.readValue(message.getBody(), AlbumCreatedEvent.class);
            handlers.forEach(eventHandler -> eventHandler.handleEvent(event));
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid message format", e);
        }
    }

    public ChannelTopic getChannelTopic() {
        return new ChannelTopic(channelName);
    }
}