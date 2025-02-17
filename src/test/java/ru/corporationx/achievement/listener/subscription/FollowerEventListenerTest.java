package ru.corporationx.achievement.listener.subscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.corporationx.achievement.dto.subscription.FollowerEvent;
import ru.corporationx.achievement.handler.subscription.FollowerEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import ru.corporationx.achievement.listener.subscription.FollowerEventListener;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerEventListenerTest {

    @Mock
    private FollowerEventHandler eventHandler;

    private List<FollowerEventHandler> handlers;

    @Mock
    private ObjectMapper objectMapper;

    private FollowerEventListener eventListener;

    @BeforeEach
    public void setUp() {
        handlers = List.of(eventHandler);
        eventListener = new FollowerEventListener(handlers, objectMapper);
    }

    @Test
    public void testOnMessage() throws IOException {
        FollowerEvent event = prepareEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), FollowerEvent.class)).thenReturn(event);
        when(message.getBody()).thenReturn(messageBody);

        eventListener.onMessage(message, messageBody);

        verify(handlers.get(0)).handleEvent(event);
    }

    @Test
    public void testOnMessageIfThrowsIOException() throws IOException {
        FollowerEvent event = prepareEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), FollowerEvent.class)).thenThrow(new IOException());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventListener.onMessage(message, messageBody));

        assertEquals("Failed to read from message body", exception.getMessage());
    }

    private FollowerEvent prepareEvent() {
        return new FollowerEvent(1L, 2L);
    }
}
