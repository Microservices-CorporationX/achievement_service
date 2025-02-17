package ru.corporationx.achievement.cache;

import ru.corporationx.achievement.dto.AchievementDto;
import ru.corporationx.achievement.mapper.AchievementMapper;
import ru.corporationx.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AchievementCache {

    private final AchievementRepository achievementRepository;
    private final AchievementMapper mapper;
    private Map<String, AchievementDto> cache = new HashMap<>();

    @PostConstruct
    public void setCache() {
        achievementRepository.findAll()
                .forEach(achievement -> cache.put(achievement.getTitle(), mapper.toDto(achievement)));
    }

    public AchievementDto get(String title) {
        return cache.get(title);
    }

}
