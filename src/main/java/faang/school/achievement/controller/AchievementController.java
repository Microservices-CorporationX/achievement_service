package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/achievement")
public class AchievementController {
    private final AchievementService achievementService;

    @PostMapping("/publish")
    public ResponseEntity<AchievementDto> publishAchievementEvent(@RequestBody AchievementDto achievementDto) {
        achievementService.publishAchievementEvent(achievementDto.getUserId(), achievementDto.getAchievementId());
        return ResponseEntity.ok().build();
    }
}
