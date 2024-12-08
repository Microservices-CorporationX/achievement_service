package faang.school.achievement.service.achievement;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementCache {

    private final AchievementRepository achievementRepository;

    private Map<String, Achievement> achievementsByTitle = new HashMap<>();

    @PostConstruct
    public void fillCache() {
        achievementRepository.findAll()
                .forEach(achievement -> achievementsByTitle.put(achievement.getTitle(), achievement));
        log.info("Achievements saved in cache");
    }

    public Achievement get(String title) {
        Achievement achievement = achievementsByTitle.get(title);
        if (achievement == null) {
            for (Achievement achievementFromDB : achievementRepository.findAll()) {
                if (achievementFromDB.getTitle().equals(title)) {
                    achievement = achievementFromDB;
                    achievementsByTitle.put(achievement.getTitle(), achievement);
                    break;
                }
            }
        }
        return achievement;
    }

    public List<Achievement> getAll() {
        return achievementsByTitle.values().stream().toList();
    }
}
