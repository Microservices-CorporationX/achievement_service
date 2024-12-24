package faang.school.achievement.publisher;

import faang.school.achievement.dto.redisevent.AchievementEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementPublisher implements MessagePublisher<AchievementEvent> {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic achievementTopic;

    @Override
    public void publish(AchievementEvent event) {
        redisTemplate.convertAndSend(achievementTopic.getTopic(), event);
        log.info("Message: {} was send in topic - {}", event, achievementTopic.getTopic());
    }
}
