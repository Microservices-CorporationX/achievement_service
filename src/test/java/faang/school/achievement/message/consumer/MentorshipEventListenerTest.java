package faang.school.achievement.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.exceptions.MessageMappingException;
import faang.school.achievement.message.event.MentorshipEvent;
import faang.school.achievement.message.handler.mentorship.MentorshipEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipEventListenerTest {

    private MentorshipEventListener mentorshipEventListener;

    @Mock
    private ObjectMapper objectMapper;

    private MentorshipEventHandler mentorshipEventHandler;
    private List<MentorshipEventHandler> mentorshipEventHandlers;

    @Mock
    private Message message;
    private Class<MentorshipEvent> eventClass;

    @BeforeEach
    void setUp() {
        mentorshipEventHandler = Mockito.mock(MentorshipEventHandler.class);
        mentorshipEventHandlers = List.of(mentorshipEventHandler);

        mentorshipEventListener = new MentorshipEventListener(mentorshipEventHandlers, objectMapper);

        eventClass = MentorshipEvent.class;
    }

    @Test
    public void testOnMessage() throws IOException {
        // arrange
        long userId = 1L;
        MentorshipEvent mentorshipEvent = new MentorshipEvent(userId);

        when(objectMapper.readValue(message.getBody(), eventClass))
                .thenReturn(mentorshipEvent);

        // act
        mentorshipEventListener.onMessage(message, new byte[]{});

        // assert
        verify(mentorshipEventHandler).handleEvent(mentorshipEvent);
    }

    @Test
    public void testOnMessageThrowsMessageMappingException() throws IOException {
        // arrange
        when(objectMapper.readValue(message.getBody(), eventClass))
                .thenThrow(new IOException());

        // act and assert
        assertThrows(MessageMappingException.class,
                () -> mentorshipEventListener.onMessage(message, new byte[]{}));
    }
}
