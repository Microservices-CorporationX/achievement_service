package faang.school.achievement.publisher;

public interface MessagePublisher <T> {
    Class<?> getInstance();
    void publish(T event);
}
