package faang.school.achievement.handler.mentorship.sensei;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.mentorship.MentorshipStartEvent;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.exception.DataValidationException;
import faang.school.achievement.handler.mentorship.MentorshipEventHandler;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SenseiAchievementHandler extends MentorshipEventHandler {

    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    @Override
    public void handleEvent(MentorshipStartEvent event) {
        log.info("Starting handleEvent for mentorId: {}", event.getMentorId());

        AchievementDto achievement = getAndValidateAchievement();

        if (achievementService.hasAchievement(event.getMentorId(), achievement.getId())) {
            log.debug("The user with ID {} already has the 'SENSEI' achievement.", event.getMentorId());
            return;
        }

        achievementService.createProgressIfNecessary(event.getMentorId(), achievement.getId());
        AchievementProgress progress = achievementService.getProgress(event.getMentorId(), achievement.getId());
        progress.setCurrentPoints(progress.getCurrentPoints() + 1);
        achievementService.saveProgress(progress);

        if (achievement.getPoints() == progress.getCurrentPoints()) {
            log.info("User with ID {} has now received the 'SENSEI' achievement.", event.getMentorId());
            achievementService.giveAchievement(achievement, event.getMentorId());
        }
        log.info("Finished handleEvent for mentorId: {}", event.getMentorId());
    }

    private AchievementDto getAndValidateAchievement() {
        AchievementDto achievement = achievementCache.get("SENSEI");

        if (achievement == null) {
            log.error("Failed to get 'SENSEI' achievement from cache.");
            throw new AchievementNotFoundException("Failed to get 'SENSEI' achievement from cache.");
        }

        return achievement;
    }
}
