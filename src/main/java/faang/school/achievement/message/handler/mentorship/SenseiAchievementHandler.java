package faang.school.achievement.handler.mentorship;

import faang.school.achievement.message.event.MentorshipEvent;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SenseiAchievementHandler extends MentorshipEventHandler {

    private final AchievementService achievementService;

    @Value("${spring.data.redis.cache.key.sensei}")
    private String senseiCacheKey;

    @Override
    public void handleEvent(MentorshipEvent event) {
        log.info("Starting handleEvent for SenseiAchievement");
        achievementService.handleAchievement(event.userId(), senseiCacheKey);
    }
}
