package faang.school.achievement.handler;

public interface EventHandler {

    void handleEvent(Long userId);

    Class<?> requiredEvent();
}
