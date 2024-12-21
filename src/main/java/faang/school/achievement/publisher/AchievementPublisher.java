package faang.school.achievement.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.redisevent.AchievementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementPublisher implements MessagePublisher<AchievementEvent> {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic achievementTopic;

    private final ObjectMapper objectMapper;

    @Override
    public Class<?> getInstance() {
        return AchievementEvent.class;
    }

    @Override
    public void publish(AchievementEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(achievementTopic.getTopic(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
