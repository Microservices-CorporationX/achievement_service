package faang.school.achievement.listener.mentorship;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.mentorship.MentorshipStartEvent;
import faang.school.achievement.handler.mentorship.MentorshipEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipEventListenerTest {

    @Mock
    private MentorshipEventHandler eventHandler;

    private List<MentorshipEventHandler> handlers;

    @Mock
    private ObjectMapper objectMapper;

    private MentorshipEventListener eventListener;

    @BeforeEach
    public void setUp() {
        handlers = List.of(eventHandler);
        eventListener = new MentorshipEventListener(handlers, objectMapper);
    }

    @Test
    public void testOnMessage() throws IOException {
        MentorshipStartEvent event = prepareEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), MentorshipStartEvent.class)).thenReturn(event);
        when(message.getBody()).thenReturn(messageBody);

        eventListener.onMessage(message, messageBody);

        verify(handlers.get(0)).handleEvent(event);
    }

    @Test
    public void testOnMessageIfThrowsIOException() throws IOException {
        MentorshipStartEvent event = prepareEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), MentorshipStartEvent.class)).thenThrow(new IOException());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventListener.onMessage(message, messageBody));

        assertEquals("Failed to read MentorshipStartEvent from message body", exception.getMessage());
    }

    private MentorshipStartEvent prepareEvent() {
        return MentorshipStartEvent.builder()
                .mentorId(1L)
                .menteeId(2L)
                .build();
    }
}
