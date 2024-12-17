package faang.school.achievement.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.event.AchievementEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementPublisherTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AchievementPublisher achievementPublisher;

    @BeforeEach
    void setUp() {
        achievementPublisher = new AchievementPublisher(redisTemplate, objectMapper);
        achievementPublisher.achievementTopic = "achievement_channel";
    }

    @Test
    void testPublishSuccess() throws JsonProcessingException {
        AchievementEvent event = AchievementEvent.builder()
                .title("Achievement Title")
                .userId(123L)
                .timestamp(LocalDateTime.now())
                .build();
        String jsonMessage = "{\"title\":\"Achievement Title\",\"userId\":123,\"timestamp\":\"2023-10-10T10:10:10\"}";

        when(objectMapper.writeValueAsString(event)).thenReturn(jsonMessage);

        achievementPublisher.publish(event);

        verify(redisTemplate, times(1)).convertAndSend("achievement_channel", jsonMessage);
    }

    @Test
    void testPublishJsonProcessingException() throws JsonProcessingException {
        AchievementEvent event = AchievementEvent.builder()
                .title("Achievement Title")
                .userId(123L)
                .timestamp(LocalDateTime.now())
                .build();

        when(objectMapper.writeValueAsString(event)).thenThrow(new JsonProcessingException("Test exception") {});

        achievementPublisher.publish(event);

        verify(redisTemplate, never()).convertAndSend(anyString(), anyString());
    }
}