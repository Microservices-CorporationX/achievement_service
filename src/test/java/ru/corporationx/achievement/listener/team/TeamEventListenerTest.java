package ru.corporationx.achievement.listener.team;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.corporationx.achievement.handler.team.TeamEventHandler;
import ru.corporationx.achievement.dto.team.TeamEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import ru.corporationx.achievement.listener.team.TeamEventListener;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamEventListenerTest {

    @Mock
    private TeamEventHandler eventHandler;

    private List<TeamEventHandler> handlers;

    @Mock
    private ObjectMapper objectMapper;

    private TeamEventListener eventListener;

    @BeforeEach
    public void setUp() {
        handlers = List.of(eventHandler);
        eventListener = new TeamEventListener(handlers, objectMapper);
    }

    @Test
    public void testOnMessage() throws IOException {
        TeamEvent event = prepareEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), TeamEvent.class)).thenReturn(event);
        when(message.getBody()).thenReturn(messageBody);

        eventListener.onMessage(message, messageBody);

        verify(handlers.get(0)).handleEvent(event);
    }

    @Test
    public void testOnMessageIfThrowsIOException() throws IOException {
        TeamEvent event = prepareEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), TeamEvent.class)).thenThrow(new IOException());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventListener.onMessage(message, messageBody));

        assertEquals("Failed to read from message body", exception.getMessage());
    }

    private TeamEvent prepareEvent() {
        return TeamEvent.builder()
                .id(1L)
                .authorId(2L)
                .projectId(3L)
                .build();
    }
}
