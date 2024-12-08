package faang.school.achievement.event_hendler;

public interface EventHandler<T> {
    void handleEvent(T event);
}
