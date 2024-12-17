package faang.school.achievement.service;

import faang.school.achievement.dto.album.AlbumCreatedEvent;
import faang.school.achievement.handler.EventHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AsyncHandlerServiceTest {

    private final AsyncHandlerService<AlbumCreatedEvent> asyncHandlerService = new AsyncHandlerService<>();
    @Mock
    private EventHandler<AlbumCreatedEvent> albumCreatedEventHandler;

    @Test
    void testHandle(){
        AlbumCreatedEvent albumCreatedEvent = new AlbumCreatedEvent();
        asyncHandlerService.handle(albumCreatedEventHandler, albumCreatedEvent);
        verify(albumCreatedEventHandler, times(1)).handleEvent(albumCreatedEvent);
    }

}