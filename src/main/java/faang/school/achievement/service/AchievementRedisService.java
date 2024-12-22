package faang.school.achievement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.model.Achievement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AchievementRedisService {

    private static final String KEY_MAP = "Achievements";

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Map<String, Achievement>> redisTemplate;

    public void saveAchievement(Map<String, Achievement> achievement) {
        redisTemplate.opsForHash().putAll(KEY_MAP, achievement);
    }

    public Achievement getAchievement(String title) {
        return objectMapper.convertValue(redisTemplate.opsForHash().get(KEY_MAP, title), Achievement.class);
    }

    public List<Achievement> getAllAchievements() {
        return redisTemplate.opsForHash().values(KEY_MAP).stream()
                .map(object -> objectMapper.convertValue(object, Achievement.class))
                .toList();
    }

    public void cleanAchievements() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().flushDb();
    }
}