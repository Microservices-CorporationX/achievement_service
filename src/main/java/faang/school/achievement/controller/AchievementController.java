package faang.school.achievement.controller;

import faang.school.achievement.service.AchievementService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/achievements")
@RequiredArgsConstructor
@Validated
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping
    public ResponseEntity<String> getAchievement(@RequestParam @NotBlank String name) {
        return ResponseEntity.ok(achievementService.getAchievementDtoFromCacheByName(name).getTitle());
    }
}