package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.SkillAcquiredEvent;
import faang.school.achievement.handler.mentorship.SkillAcquiredHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SkillEventListener implements MessageListener {
    private final List<SkillAcquiredHandler> skillAcquiredHandler;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            SkillAcquiredEvent event = objectMapper.readValue(message.getBody(), SkillAcquiredEvent.class);
            skillAcquiredHandler.forEach(handler -> handler.handleEvent(event));
        } catch (IOException e) {
            log.error("JSON processing exception " + e);
            throw new RuntimeException(e);
        }
    }
}
