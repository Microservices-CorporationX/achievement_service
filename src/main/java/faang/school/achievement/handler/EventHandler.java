package faang.school.achievement.handler;

public interface EventHandler<E> {
    void handleEvent(E event);
}
