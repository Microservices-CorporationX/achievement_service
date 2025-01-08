package faang.school.achievement.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.comment.CommentEvent;
import faang.school.achievement.handler.comment.CommentEventHandler;
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
public class CommentEventListenerTest {

    @Mock
    private CommentEventHandler eventHandler;

    private List<CommentEventHandler> handlers;

    @Mock
    private ObjectMapper objectMapper;

    private CommentEventListener eventListener;

    @BeforeEach
    public void setUp() {
        handlers = List.of(eventHandler);
        eventListener = new CommentEventListener(handlers, objectMapper);
    }

    @Test
    public void testOnMessage() throws IOException {
        CommentEvent event = prepareEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), CommentEvent.class)).thenReturn(event);
        when(message.getBody()).thenReturn(messageBody);

        eventListener.onMessage(message, messageBody);

        verify(handlers.get(0)).handleEvent(event);
    }

    @Test
    public void testOnMessageIfThrowsIOException() throws IOException {
        CommentEvent event = prepareEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), CommentEvent.class)).thenThrow(new IOException());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventListener.onMessage(message, messageBody));

        assertEquals("Failed to read from message body", exception.getMessage());
    }

    private CommentEvent prepareEvent() {
        return CommentEvent.builder()
                .authorId(1L)
                .commentId(2L)
                .postId(3L)
                .content("1234")
                .build();
    }
}
