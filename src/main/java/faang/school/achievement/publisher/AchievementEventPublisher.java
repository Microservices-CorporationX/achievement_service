package faang.school.achievement.publisher;

import faang.school.achievement.dto.AchievementEventDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class AchievementEventPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    @Qualifier("achievementChannelTopic")
    private final ChannelTopic achievementChannelTopic;

    public void publish(AchievementEventDto message) {
        redisTemplate.convertAndSend(achievementChannelTopic.getTopic(), message);
        log.info("Message published: {}", message);
    }
}
