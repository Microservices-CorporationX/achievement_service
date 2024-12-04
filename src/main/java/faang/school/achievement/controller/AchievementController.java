package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.service.AchievementCache;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/achievements")
@RequiredArgsConstructor
public class AchievementController {
    private final AchievementCache achievementCache;

    @GetMapping("/{achievement}")
    public AchievementDto getAchievement(@PathVariable("achievement") String title) {
        return achievementCache.get(title);
    }

    @GetMapping
    public List<AchievementDto> getAchievement() {
        return achievementCache.getAll();
    }

}
