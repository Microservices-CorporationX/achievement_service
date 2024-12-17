package faang.school.achievement.service.achievement;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.mapper.achievement.AchievementMapper;
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
public class AchievementCache {

    private static final String KEY_MAP = "Achievements";

    private final AchievementRepository achievementRepository;
    private final AchievementRedisService redisService;

    @PostConstruct
    public void fillCache() {
        Map<String, Achievement> achievementsByTitle = new HashMap<>();
        achievementRepository.findAll()
                .forEach(achievement -> achievementsByTitle.put(achievement.getTitle(), achievement));

        redisService.saveAchievement(KEY_MAP, achievementsByTitle);
        log.info("Achievements saved in cache");
    }

    @PreDestroy
    private void clearingCache() {
        redisService.cleanAchievements();
        log.info("Achievement cache cleared");
    }


    public Achievement get(String title) {
        Achievement achievement = redisService.getAchievement(KEY_MAP, title);
        if (achievement == null) {
            clearingCache();
            fillCache();
            achievement = redisService.getAchievement(KEY_MAP, title);
            if (achievement == null) {
                throw new NoSuchElementException("Achievement with title " + title + " no such");
            }
        }
        return achievement;
    }

    public List<Achievement> getAll() {
        return redisService.getAllAchievements(KEY_MAP);
    }
}
