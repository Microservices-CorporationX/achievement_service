package ru.corporationx.achievement.listener.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.corporationx.achievement.dto.project.ProjectEvent;
import ru.corporationx.achievement.handler.project.ProjectEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import ru.corporationx.achievement.listener.project.ProjectEventListener;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ProjectEventHandler projectEventHandler;

    private ProjectEventListener projectEventListener;

    @BeforeEach
    public void setUp() {
        projectEventListener = new ProjectEventListener(List.of(projectEventHandler), objectMapper);
    }

    @Test
    public void shouldInvokeHandleEvent() throws IOException {
        ProjectEvent projectEvent = getProjectEvent();
        Message message = mock(Message.class);
        when(objectMapper.readValue(message.getBody(), ProjectEvent.class)).thenReturn(projectEvent);

        projectEventListener.onMessage(message, null);

        verify(projectEventHandler).handleEvent(projectEvent);
    }

    @Test
    public void shouldWrapIOExceptionInRuntimeException() throws IOException {
        Message message = mock(Message.class);
        when(objectMapper.readValue(message.getBody(), ProjectEvent.class)).thenThrow(IOException.class);

        assertThrows(RuntimeException.class, () -> projectEventListener.onMessage(message, null));
    }

    private static ProjectEvent getProjectEvent() {
        return ProjectEvent.builder()
                .ownerId(1L)
                .projectId(1L)
                .build();
    }
}