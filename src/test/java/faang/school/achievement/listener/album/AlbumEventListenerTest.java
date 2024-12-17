package faang.school.achievement.listener.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.album.AlbumCreatedEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.service.AsyncHandlerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlbumEventListenerTest {

    @InjectMocks
    private AlbumEventListener albumEventListener;

    private List<EventHandler<AlbumCreatedEvent>> handlers;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AsyncHandlerService<AlbumCreatedEvent> asyncHandlerService;
    @Mock
    private EventHandler<AlbumCreatedEvent> albumCreatedEventHandler;
    @Mock
    private Message message;

    @BeforeEach
    void setUp() {
        handlers = new ArrayList<>(List.of(albumCreatedEventHandler));
        albumEventListener = new AlbumEventListener(handlers, objectMapper, asyncHandlerService);
    }

    @Test
    public void testOnMessage() throws IOException {
        AlbumCreatedEvent event = prepareEvent();
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), AlbumCreatedEvent.class)).thenReturn(event);
        when(message.getBody()).thenReturn(messageBody);

        albumEventListener.onMessage(message, messageBody);

        verify(asyncHandlerService).handle(handlers.get(0), event);
    }

    @Test
    public void testOnMessageIfThrowsIOException() throws IOException {
        AlbumCreatedEvent event = prepareEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), AlbumCreatedEvent.class)).thenThrow(new IOException());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> albumEventListener.onMessage(message, messageBody));

        assertEquals("Failed to read album creation event from message body", exception.getMessage());
    }

    private AlbumCreatedEvent prepareEvent() {
        return AlbumCreatedEvent.builder()
                .userId(1L)
                .albumId(2L)
                .albumTitle("Test")
                .build();
    }

}