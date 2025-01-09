package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.MentorshipAcceptedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipAcceptedEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Message message;

    @InjectMocks
    MentorshipAcceptedEventListener mentorshipAcceptedEventListener;

    @Test
    @DisplayName("Test onMessage: successful deserialization")
    void testOnMessage_SuccessfulDeserialization() throws IOException {
        byte[] messageBody = "{\"mentorId\": 1, \"menteeId\": 2}".getBytes(StandardCharsets.UTF_8);
        when(message.getBody()).thenReturn(messageBody);

        MentorshipAcceptedEvent event = MentorshipAcceptedEvent.builder()
                .requesterId(1L)
                .receiverId(2l)
                .build();

        when(objectMapper.readValue(messageBody, MentorshipAcceptedEvent.class)).thenReturn(event);

        mentorshipAcceptedEventListener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(messageBody, MentorshipAcceptedEvent.class);
    }

    @Test
    @DisplayName("Test onMessage: deserialization error")
    void testOnMessage_DeserializationError() throws IOException {
        byte[] messageBody = "invalid-json".getBytes(StandardCharsets.UTF_8);
        when(message.getBody()).thenReturn(messageBody);
        IOException exception = new IOException("Deserialization failed");
        when(objectMapper.readValue(messageBody, MentorshipAcceptedEvent.class)).thenThrow(exception);

        mentorshipAcceptedEventListener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(messageBody, MentorshipAcceptedEvent.class);
    }
}