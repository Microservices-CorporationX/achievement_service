package faang.school.achievement.publisher;

import faang.school.achievement.dto.redisevent.AchievementEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AchievementPublisherTest {

    private static final String TOPIC_NAME = "achievement_channel";

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ChannelTopic achievementTopic;

    @InjectMocks
    private AchievementPublisher achievementPublisher;

    private final long userId = 1L;
    private final String achievementText = "achievement text";


    @Test
    void publishSuccessTest() {
        AchievementEvent achievementEvent = AchievementEvent.builder()
                .userId(userId)
                .achievement(achievementText)
                .build();

        when(achievementTopic.getTopic()).thenReturn(TOPIC_NAME);
        assertDoesNotThrow(() -> achievementPublisher.publish(achievementEvent));
        verify(redisTemplate).convertAndSend(TOPIC_NAME, achievementEvent);
    }
}