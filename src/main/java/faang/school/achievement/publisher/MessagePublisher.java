package faang.school.achievement.publisher;

public interface MessagePublisher <T> {
    void publish(T event);
}
