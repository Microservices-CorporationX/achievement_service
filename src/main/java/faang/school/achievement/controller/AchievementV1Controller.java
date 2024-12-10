package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.service.AchievementService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/achievements")
public class AchievementV1Controller {

    private final AchievementService achievementService;

    @PostMapping
    public List<AchievementDto> getAllPossibleAchievements(@RequestBody AchievementFilterDto filterDto) {
        return achievementService.getAllPossibleAchievements(filterDto);
    }

    @GetMapping("/user/{userId}")
    public List<AchievementDto> getAllUserAchievements(@PathVariable @Positive long userId) {
        return achievementService.getAllUserAchievements(userId);
    }

    @GetMapping("/user/{userId}/unfinished")
    public List<AchievementProgressDto> getUnfinishedUserAchievements(@PathVariable @Positive long userId) {
        return achievementService.getUnfinishedUserAchievements(userId);
    }

    @GetMapping("/{achievementId}")
    public AchievementDto getAchievement(@PathVariable @Positive long achievementId) {
        return achievementService.getAchievement(achievementId);
    }
}
