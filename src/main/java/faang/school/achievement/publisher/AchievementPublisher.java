package faang.school.achievement.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.event.AchievementEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementPublisher implements MessagePublisher<AchievementEvent> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ChannelTopic achievementTopic;

    @Override
    public void publish(AchievementEvent message) {
        try {
            redisTemplate.convertAndSend(achievementTopic.getTopic(), objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            log.error("Json processing exception", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
