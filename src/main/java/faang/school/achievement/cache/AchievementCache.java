package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class AchievementCache {

    private final AchievementRepository achievementRepository;

    private final Map<String, Achievement> achievementMap = new HashMap<>();

    @Autowired
    public AchievementCache(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
        initializeCache();
        log.info("Achievement cache initialized");
    }

    private void initializeCache() {
        achievementRepository.findAll().forEach(achievement ->
                achievementMap.put(achievement.getTitle(), achievement));
    }

    public Optional<Achievement> getAchievement(String title) {
        log.info("Getting achievement {}", title);
        return Optional.ofNullable(achievementMap.get(title));
    }
}