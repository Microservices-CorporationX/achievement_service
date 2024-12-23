package faang.school.achievement.controller;

import faang.school.achievement.dto.redisevent.AchievementEvent;
import faang.school.achievement.publisher.AchievementPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
@RequiredArgsConstructor
public class TestController {
    private final AchievementPublisher achievementPublisher;

    @PutMapping("/api/achievement-service/event")
    public void sendEvent(@RequestBody AchievementEvent event) {
        achievementPublisher.publish(event);
    }
}
