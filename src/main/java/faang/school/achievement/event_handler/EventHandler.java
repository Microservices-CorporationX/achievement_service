package faang.school.achievement.event_handler;

public interface EventHandler<T> {
    void handleEvent(T event);
}
