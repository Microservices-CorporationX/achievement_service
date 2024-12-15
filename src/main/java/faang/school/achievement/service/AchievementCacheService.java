package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AchievementCacheService implements CommandLineRunner {
    private final AchievementRepository achievementRepository;
    private final AchievementService achievementService;

    @Override
    public void run(String... args) throws Exception {
        Iterable<Achievement> achievements = achievementRepository.findAll();
        for (Achievement achievement : achievements) {
            achievementService.getAchievementDtoFromCacheByName(achievement.getTitle());
            log.info("Preloaded achievement: {}", achievement.getTitle());
        }
    }
}