package faang.school.achievement.service;

import faang.school.achievement.exception.CachePrefetchException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AchievementCacheWarmupService implements CommandLineRunner {
    private final AchievementRepository achievementRepository;
    private final AchievementService achievementService;

    @Override
    public void run(String... args) {
        try {
            achievementRepository.findAll().parallelStream()
                    .forEach(this::preloadAchievement);
        } catch (CachePrefetchException e) {
            log.error("Failed to preload achievements: {}", e.getMessage());
            throw e;
        }
    }

    private void preloadAchievement(Achievement achievement) {
        achievementService.getAchievementByTitle(achievement.getTitle());
        log.info("Preloaded achievement: {}", achievement.getTitle());
    }
}