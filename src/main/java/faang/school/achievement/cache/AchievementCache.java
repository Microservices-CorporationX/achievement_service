package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AchievementCache {

    private final AchievementRepository achievementRepository;
    private Map<String, Achievement> cache = new HashMap<>();

    @PostConstruct
    public void setCache() {
        achievementRepository.findAll()
                .forEach(achievement -> cache.put(achievement.getTitle(), achievement));
    }

    public Achievement get(String title) {
        return cache.get(title);
    }

}