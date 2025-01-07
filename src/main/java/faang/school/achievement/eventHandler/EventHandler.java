package faang.school.achievement.eventHandler;

import org.springframework.stereotype.Component;

@Component
public interface EventHandler <T> {
     void handle (T event);
}
