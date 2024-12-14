package faang.school.achievement.handler;

public interface EventHandler<T> {
    boolean canHandleEventType(Class<?> eventType);
    void handleEvent(T event);
}
