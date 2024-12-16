package faang.school.achievement.service;

import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AsyncHandlerService<T> {

    @Async("taskExecutor")
    public void handle(EventHandler<T> handler, T event) {
        handler.handleEvent(event);
    }
}
