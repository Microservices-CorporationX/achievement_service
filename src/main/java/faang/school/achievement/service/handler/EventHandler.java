package faang.school.achievement.service.handler;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface EventHandler<T> {
    void process(T event) throws JsonProcessingException;
}
