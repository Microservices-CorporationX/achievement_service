package ru.corporationx.achievement.listener.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.corporationx.achievement.dto.recommendation.RecommendationEvent;
import ru.corporationx.achievement.handler.recommendation.RecommendationEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import ru.corporationx.achievement.listener.recommendation.RecommendationEventListener;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationEventListenerTest {

    @Mock
    private RecommendationEventHandler eventHandler;

    private List<RecommendationEventHandler> handlers;

    @Mock
    private ObjectMapper objectMapper;

    private RecommendationEventListener eventListener;

    @BeforeEach
    public void setUp() {
        handlers = List.of(eventHandler);
        eventListener = new RecommendationEventListener(handlers, objectMapper);
    }

    @Test
    public void testOnMessage() throws IOException {
        RecommendationEvent event = prepareEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), RecommendationEvent.class)).thenReturn(event);
        when(message.getBody()).thenReturn(messageBody);

        eventListener.onMessage(message, messageBody);

        verify(handlers.get(0)).handleEvent(event);
    }

    @Test
    public void testOnMessageIfThrowsIOException() throws IOException {
        RecommendationEvent event = prepareEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), RecommendationEvent.class)).thenThrow(new IOException());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventListener.onMessage(message, messageBody));

        assertEquals("Failed to read from message body", exception.getMessage());
    }

    private RecommendationEvent prepareEvent() {
        return RecommendationEvent.builder()
                .authorId(1L)
                .receiverId(2L)
                .build();
    }
}
