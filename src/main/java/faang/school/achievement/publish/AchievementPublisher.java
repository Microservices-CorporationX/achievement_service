package faang.school.achievement.publish;

import faang.school.achievement.dto.AchievementEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class AchievementPublisher extends RedisMessagePublisher<AchievementEvent> {
    public AchievementPublisher(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }
}
