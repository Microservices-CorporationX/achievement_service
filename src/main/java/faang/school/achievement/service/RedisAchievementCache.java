package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisAchievementCache implements Cache<Achievement> {

    private final AchievementRepository achievementRepository;
    private final AchievementRedisService redisService;

    @PostConstruct
    private void fillCache() {
        Map<String, Achievement> achievementsByTitle = new HashMap<>();
        achievementRepository.findAll()
                .forEach(achievement -> achievementsByTitle.put(achievement.getTitle(), achievement));

        redisService.saveAchievement(achievementsByTitle);
        log.info("Achievements saved in cache");
    }

    @PreDestroy
    private void clearCache() {
        redisService.cleanAchievements();
        log.info("Achievement cache cleared");
    }


    public Achievement get(String title) {
        Achievement achievement = redisService.getAchievement(title);
        if (achievement == null) {
            clearCache();
            fillCache();
            achievement = redisService.getAchievement(title);
            if (achievement == null) {
                throw new NoSuchElementException("Achievement with title " + title + " no such");
            }
        }
        return achievement;
    }

    public List<Achievement> getAll() {
        return redisService.getAllAchievements();
    }
}
