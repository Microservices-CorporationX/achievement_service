package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/achievements")
@RequiredArgsConstructor
public class AchievementController {
    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    @GetMapping("/{achievement}")
    public AchievementDto getAchievement(@PathVariable("achievement") String title) {
        return achievementCache.get(title);
    }

    @GetMapping
    public List<AchievementDto> getAchievement() {
        return achievementCache.getAll();
    }

    // Just a test method for event publishing. You can delete it freely.
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void giveAchievement(@RequestParam("user-id") Long userId,
                                @RequestParam("title") String achievementTitle) {
        achievementService.giveAchievement(userId, achievementTitle);
    }
}
