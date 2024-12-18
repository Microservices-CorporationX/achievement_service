package faang.school.achievement.publisher;

import faang.school.achievement.event.AchievementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementPublisher {

    @Value("${spring.data.redis.channel.achievement}")
    private String achievementChannel;

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(AchievementEvent event) {
        redisTemplate.convertAndSend(achievementChannel, event);
    }
}
