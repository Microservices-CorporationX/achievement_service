package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping("/all")
    public List<AchievementDto>  getAll(@RequestBody AchievementDto filters) {
        return achievementService.getAll(filters);
    }

    @GetMapping
    public List<AchievementDto> getByUserId(@RequestParam("user") Long userId) {
        return achievementService.getByUserId(userId);
    }

    @GetMapping("/{achievementId}")
    public AchievementDto get(@PathVariable Long achievementId) {
        return achievementService.get(achievementId);
    }

    @GetMapping("/{userId}/unearned")
    public List<AchievementDto> getByUserIdUnearned(@PathVariable Long userId) {
        return achievementService.getByUserIdUnearned(userId);
    }
}
