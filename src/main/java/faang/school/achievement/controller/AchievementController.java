package faang.school.achievement.controller;

import faang.school.achievement.service.AchievementService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/achievements")
@Validated
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping
    public ResponseEntity<String> getAchievement(@RequestParam @NotBlank(message = "Name is required") String title) {
        return ResponseEntity.ok(achievementService.getAchievementByTitle(title).getTitle());
    }

    @PostMapping("/{achievementId}/user/{userId}")
    public ResponseEntity<Void> processAchievementForUser(@PathVariable long userId,
                                                          @PathVariable long achievementId) {
        achievementService.processAchievementForUser(userId, achievementId);
        return ResponseEntity.ok().build();
    }
}
