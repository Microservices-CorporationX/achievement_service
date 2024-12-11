package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.AllLoveAchievementHandler;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener, RedisContainerMessageListener{

    private final AchievementService achievementService;
    private final ObjectMapper objectMapper;
    private final List<AllLoveAchievementHandler> handlers;

    public void processEvent(Message message, Class<T> eventType) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
//            processAchievement(event);
        } catch (Exception e) {
            log.error("Error processing event: {}", message, e);
        }
    }
}
