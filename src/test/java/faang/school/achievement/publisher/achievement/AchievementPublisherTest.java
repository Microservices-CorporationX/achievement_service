package faang.school.achievement.publisher.achievement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.redis.RedisTopicsFactory;
import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.publisher.redis.achievement.AchievementPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.Topic;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementPublisherTest {
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private RedisTopicsFactory redisTopicsFactory;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private Topic topic;

    private AchievementPublisher achievementPublisher;
    private final String achievementChannelName = "achievementChannel";

    @BeforeEach
    void setUp() {
        when(redisTopicsFactory.getTopic(achievementChannelName)).thenReturn(topic);
        achievementPublisher = new AchievementPublisher(
                redisTemplate,
                objectMapper,
                redisTopicsFactory,
                achievementChannelName
        );
    }

    @Test
    void publishAchievementEvent_shouldPublishAchievementEventToRedis() throws JsonProcessingException {
        AchievementEvent event = new AchievementEvent(
                1L,
                "Str",
                "AchievementTitle",
                "Description",
                1,
                LocalDateTime.parse("2024-10-14T23:07:09"),
                LocalDateTime.parse("2024-10-14T23:07:09")
        );

        String eventJson = "{" +
                "\"id\":1," +
                "\"title\":\"Str\"," +
                "\"description\":\"AchievementTitle\"," +
                "\"rarity\":\"Description\"," +
                "\"points\":1," +
                "\"createdAt\":\"2024-10-14T23:07:09\"," +
                "\"updatedAt\":\"2024-10-14T23:07:09\"" +
                "}";

        when(objectMapper.writeValueAsString(event)).thenReturn(eventJson);
        when(topic.getTopic()).thenReturn(achievementChannelName);

        achievementPublisher.publishAchievementEvent(event);

        verify(redisTemplate, times(1)).convertAndSend(achievementChannelName, eventJson);
    }
}