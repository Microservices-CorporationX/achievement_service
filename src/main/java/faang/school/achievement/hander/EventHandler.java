package faang.school.achievement.hander;

public interface EventHandler<E> {
    void handleEvent(E event);
}
