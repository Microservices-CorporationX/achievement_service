package faang.school.achievement.handler;

import faang.school.achievement.event.RecommendationEvent;
import faang.school.achievement.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NiceGuyAchievementHandler extends RecommendationEventHandler {
    private final TransactionService transactionService;

    @Override
    @Async
    @Retryable(retryFor = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 2000, multiplier = 2))
    public void handleEvent(RecommendationEvent event) {
        transactionService.handle(event);
    }
}

