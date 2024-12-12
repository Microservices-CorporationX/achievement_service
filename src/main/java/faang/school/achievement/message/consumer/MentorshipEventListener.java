package faang.school.achievement.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.mentorship.MentorshipEventHandler;
import faang.school.achievement.message.event.MentorshipEvent;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MentorshipEventListener implements MessageListener {

    private final List<MentorshipEventHandler> mentorshipEventHandlers;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipEvent mentorshipEvent = objectMapper.readValue(message.getBody(), MentorshipEvent.class);
            mentorshipEventHandlers.forEach(mentorshipEventHandler
                    -> mentorshipEventHandler.handleEvent(mentorshipEvent));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
