package faang.school.achievement.handler;

public interface EventHandler<T> {
    boolean handleEvent(T event);
}
