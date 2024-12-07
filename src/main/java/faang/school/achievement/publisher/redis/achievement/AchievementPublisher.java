package faang.school.achievement.publisher.redis.achievement;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.redis.RedisTopicsFactory;
import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.publisher.redis.AbstractEventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;

@Component
public class AchievementPublisher extends AbstractEventPublisher<AchievementEvent> {
    private final Topic achievementChannel;

    public AchievementPublisher(
            RedisTemplate<String, Object> redisTemplate,
            ObjectMapper objectMapper,
            RedisTopicsFactory redisTopicsFactory,
            @Value("${spring.data.redis.channel.achievement}") String achievementChannel) {
        super(redisTemplate, objectMapper);
        this.achievementChannel = redisTopicsFactory.getTopic(achievementChannel);
    }

    public void publishAchievementEvent(AchievementEvent event) {
        publish(event, achievementChannel);
    }
}
