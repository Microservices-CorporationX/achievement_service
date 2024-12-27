package faang.school.achievement.handler.recommendation;

import faang.school.achievement.dto.recommendation.RecommendationEvent;
import faang.school.achievement.handler.EventHandler;
import org.springframework.scheduling.annotation.Async;

public abstract class RecommendationEventHandler implements EventHandler<RecommendationEvent> {

    @Async("taskExecutor")
    public abstract void handleEvent(RecommendationEvent event);
}
