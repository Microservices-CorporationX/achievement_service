package faang.school.achievement.cache;

import faang.school.achievement.handler.AchievementDto;
import faang.school.achievement.mapper.AchievementMapper;
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
