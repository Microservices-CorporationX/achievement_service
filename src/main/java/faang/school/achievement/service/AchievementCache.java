package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.mapper.achievement.AchievementMapper;
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
    private final AchievementMapper achievementMapper;

    private Map<String, Achievement> achievementsByTitle = new HashMap<>();

    @PostConstruct
    public void fillCache() {
        log.info("Achievements saved in cache");
        achievementRepository.findAll()
                .forEach(achievement -> achievementsByTitle.put(achievement.getTitle(), achievement));
    }

    public Achievement getAchievement(String title) {
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

    public AchievementDto get(String title) {
        return achievementMapper.toDto(getAchievement(title));
    }

    public List<AchievementDto> getAll() {
        return achievementMapper.toDtoList(achievementsByTitle.values().stream().toList());
    }
}
