package faang.school.achievement.handler.collectorachievement;

import faang.school.achievement.event.Event;
import faang.school.achievement.event.goal.GoalSetEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.utils.AsyncEventHandlerProcessor;
import faang.school.achievement.handler.EventHandler;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CollectorAchievementHandler extends AbstractEventHandler implements EventHandler<GoalSetEvent> {
    @Value("${achievements.collector.title}")
    protected String title;

    public CollectorAchievementHandler(AsyncEventHandlerProcessor processor) {
        super(processor);
    }

    @Override
    public void handle(@NonNull GoalSetEvent event) {
        handle(event.userId(), title);
    }

    @Override
    public boolean isApplicable(Event event) {
        return event instanceof GoalSetEvent;
    }
}
