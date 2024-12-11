package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.service.AchievementService;
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
    private final AchievementService achievementService;

    @GetMapping("/{achievement}")
    public AchievementDto getAchievement(@PathVariable("achievement") String title) {
        return achievementService.get(title);
    }

    @GetMapping
    public List<AchievementDto> getAchievement() {
        return achievementService.getAll();
    }
}