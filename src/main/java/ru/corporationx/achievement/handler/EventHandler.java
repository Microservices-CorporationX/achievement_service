package ru.corporationx.achievement.handler;

public interface EventHandler<T> {
    void handleEvent(T event);
}
