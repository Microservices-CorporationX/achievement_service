package faang.school.achievement.handler;

import faang.school.achievement.utils.AsyncEventHandlerProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class AbstractEventHandler {
    private final AsyncEventHandlerProcessor asyncProcessor;

    public void handle(Long userId, String title) {
        asyncProcessor.process(userId, title);
    }
}
