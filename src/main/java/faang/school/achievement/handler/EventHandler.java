package faang.school.achievement.handler;

import faang.school.achievement.event.Event;

public interface EventHandler<T> {
    void handle(T event);
    boolean isApplicable(Event event);
}
