package faang.school.achievement.publisher;

import faang.school.achievement.dto.event.AchievementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementEventPublisher implements EventPublisher<AchievementEvent> {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.data.redis.channel.achievement}")
    private String achievementTopic;

    @Override
    public void publish(AchievementEvent event) {
        redisTemplate.convertAndSend(achievementTopic, event);
    }
}
