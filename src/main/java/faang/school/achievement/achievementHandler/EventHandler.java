package faang.school.achievement.achievementHandler;

public interface EventHandler<T> {
    void handleEvent(T event);
}
