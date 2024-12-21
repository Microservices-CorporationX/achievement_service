package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.MentorshipStartEvent;
import faang.school.achievement.handler.mentorship.MentorshipStartEventHandler;
import faang.school.achievement.handler.mentorship.SenseiAchievementHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipStartEventListenerTest {
    @Test
    public void testSenseiAchievementHandlerInvoke() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        SenseiAchievementHandler mockSensei = mock(SenseiAchievementHandler.class);
        List<MentorshipStartEventHandler> handlers = List.of(mockSensei);

        MentorshipStartEventListener mentorshipStartEventListener = new MentorshipStartEventListener(objectMapper,handlers);

        Message message = mock(Message.class);
        byte[] mockBody = {};
        MentorshipStartEvent mockEvent = mock(MentorshipStartEvent.class);

        when(message.getBody()).thenReturn(mockBody);
        when(objectMapper.readValue(mockBody, MentorshipStartEvent.class)).thenReturn(mockEvent);

        mentorshipStartEventListener.onMessage(message, null);
        verify(mockSensei,times(1)).handleEvent(mockEvent);
    }
}