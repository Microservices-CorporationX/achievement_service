package faang.school.achievement.controller;

import faang.school.achievement.dto.achievement.AchievementDto;
import faang.school.achievement.dto.achievement.AchievementFilterDto;
import faang.school.achievement.dto.achievement.AchievementProgressDto;
import faang.school.achievement.dto.achievement.UserAchievementDto;
import faang.school.achievement.service.AchievementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/achievement")
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping
    public List<AchievementDto> getAchievementsByFilter(@Valid @RequestBody AchievementFilterDto achievementFilterDto) {
        return achievementService.getAchievementsByFilter(achievementFilterDto);
    }

    @GetMapping("/users")
    public List<UserAchievementDto> getAchievementsByUsedId() {
        return achievementService.getAchievementsByUserId();
    }

    @GetMapping("/{id}")
    public AchievementDto getAchievementById(@PathVariable long id) {
        return achievementService.getAchievementById(id);
    }

    @GetMapping("/users/progress")
    public List<AchievementProgressDto> getAchievementProgressByUserId() {
        return achievementService.getAchievementProgressByUserId();
    }
}
