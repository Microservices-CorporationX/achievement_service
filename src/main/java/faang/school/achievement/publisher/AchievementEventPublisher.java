package faang.school.achievement.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.achievement.AchievementEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AchievementEventPublisher extends AbstractEventPublisher<AchievementEvent>{

    public AchievementEventPublisher(ObjectMapper objectMapper,
                                     RedisTemplate<String, Object> redisTemplate) {
        super(objectMapper, redisTemplate);
    }

    @Value("${spring.data.redis.channel.achievement")
    public void setChannel(String channel) {
        super.setChannel(channel);
    }
}
