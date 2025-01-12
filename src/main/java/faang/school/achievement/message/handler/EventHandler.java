package faang.school.achievement.message.handler;

public interface EventHandler<T> {

    void handleEvent(T event);
}
