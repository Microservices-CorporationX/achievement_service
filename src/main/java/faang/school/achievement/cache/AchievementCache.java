package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementCache {
    private final AchievementRepository achievementRepository;
    private Map<String, Achievement> cache = new ConcurrentHashMap<>();

    @PostConstruct
    private void initCache() {
        cache = achievementRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Achievement::getTitle, achievement -> achievement));
    }

    public Achievement getAchievementByTitle(String title) {
        if (cache.containsKey(title)) {
            return cache.get(title);
        } else {
            log.error("Title {} does not exist", title);
            throw new NoSuchElementException(String.format("Achievement with title: %s does not exist", title));
        }
    }
}
