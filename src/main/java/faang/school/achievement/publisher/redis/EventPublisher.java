package faang.school.achievement.publisher.redis;

import org.springframework.data.redis.listener.Topic;

public interface EventPublisher<T> {
    void publish(T event, Topic topic);
}
