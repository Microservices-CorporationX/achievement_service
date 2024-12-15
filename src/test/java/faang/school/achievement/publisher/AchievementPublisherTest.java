package faang.school.achievement.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.event.AchievementEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementPublisherTest {

    @InjectMocks
    private AchievementPublisher eventPublisher;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ChannelTopic achievementTopic;

    @Test
    public void testPublisher() throws JsonProcessingException {
        AchievementEvent event = new AchievementEvent();

        when(achievementTopic.getTopic()).thenReturn("topic");
        when(objectMapper.writeValueAsString(event)).thenReturn("JSON");

        eventPublisher.publish(event);

        verify(redisTemplate).convertAndSend("topic", "JSON");
    }
}
