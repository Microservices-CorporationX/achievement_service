package faang.school.achievement.publisher;

import faang.school.achievement.config.redis.RedisConfigProperties;
import faang.school.achievement.event.AchievementEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AchievementEvenPublisherTest {
    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private AchievementEventPublisher achievementEventPublisher;

    RedisConfigProperties.Channel channels
            = new RedisConfigProperties.Channel("achievement", "follower", "skill");

    RedisConfigProperties redisConfigProperties
            = new RedisConfigProperties("localhost", 6379, channels);

    @Test
    @DisplayName("Test publish achievement event: success")
    void testPublishAchievementEvent_Success() {

        AchievementEvent event = new AchievementEvent("user", 1L, 1L, "Test Achievement");

        achievementEventPublisher = new AchievementEventPublisher(redisTemplate, redisConfigProperties);

        achievementEventPublisher.publish(event);

        verify(redisTemplate, times(1)).convertAndSend(eq("achievement"), eq(event));
    }

    @Test
    @DisplayName("Test publish achievement event: failure")
    void testPublishAchievementEvent_Failure() {

        AchievementEvent event = new AchievementEvent("user", 1L, 1L, "Test Achievement");

        achievementEventPublisher = new AchievementEventPublisher(redisTemplate, redisConfigProperties);

        doThrow(new RuntimeException("Redis exception")).when(redisTemplate).convertAndSend(anyString(), any());

        achievementEventPublisher.publish(event);

        verify(redisTemplate, times(1)).convertAndSend(anyString(), any());
    }
}
