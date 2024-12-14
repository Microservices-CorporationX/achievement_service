package faang.school.achievement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.MentorshipAcceptedEvent;
import faang.school.achievement.handler.mentorship.SenseyAchievementHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipEventListenerTest {
    @InjectMocks
    private MentorshipEventListener mentorshipEventListener;
    @Mock
    private SenseyAchievementHandler senseyAchievementHandler;
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private Message message;
    private String json = "{\n" +
            "  \"requesterUserId\": 12345,\n" +
            "  \"requesterUserId\": 67890,\n" +
            "  \"userContextId\": 112233\n" +
            "}";

    @BeforeEach
    void setUp() {
        mentorshipEventListener = new MentorshipEventListener(List.of(senseyAchievementHandler), objectMapper);
    }

    @Test
    void testOnMessage() throws JsonProcessingException {
        when(message.getBody()).thenReturn(json.getBytes());
        mentorshipEventListener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(anyString(), Mockito.eq(MentorshipAcceptedEvent.class));
        verify(senseyAchievementHandler).startHandling(any(MentorshipAcceptedEvent.class));

    }
}