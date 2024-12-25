package faang.school.achievement.listener.invitation;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.achievementHandler.invitation.InvitationEventHandler;
import faang.school.achievement.dto.invitation.InviteSentEvent;
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
public class InviteEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<InvitationEventHandler> handlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            InviteSentEvent event = objectMapper.readValue(message.getBody(), InviteSentEvent.class);
            handlers.forEach(eventHandler -> eventHandler.handleEvent(event));
        } catch (IOException e) {
            log.error("Error reading value from {}", message.getChannel());
            throw new RuntimeException("Failed to read invite sent event");
        }
    }
}
