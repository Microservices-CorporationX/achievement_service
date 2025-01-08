package faang.school.achievement.handler.mentorship.sensei;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.mentorship.MentorshipStartEvent;
import faang.school.achievement.exception.AchievementNotFoundException;
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
        log.info("Starting handle event for user with ID {}", event.getMentorId());

        AchievementDto achievementDto = getAndValidateAchievement("SENSEI");

        if (achievementService.hasAchievement(event.getMentorId(), achievementDto.getId())) {
            log.debug("The user with ID {} already has the {} achievement.", event.getMentorId(), achievementDto.getTitle());
            return;
        }

        achievementService.createProgressIfNecessary(event.getMentorId(), achievementDto.getId());
        AchievementProgress achievementProgress = achievementService.getProgress(event.getMentorId(), achievementDto.getId());
        achievementProgress.increment();
        achievementService.saveProgress(achievementProgress);

        hasComplete(achievementDto, achievementProgress, event);
    }

    private AchievementDto getAndValidateAchievement(String achievementTitle) {
        AchievementDto achievement = achievementCache.get(achievementTitle);

        if (achievement == null) {
            log.error("Failed to get {} achievement from cache.", achievementTitle);
            throw new AchievementNotFoundException("Failed to get achievement from cache.");
        }

        return achievement;
    }

    private void hasComplete(AchievementDto achievementDto, AchievementProgress achievementProgress, MentorshipStartEvent event) {
        if (achievementDto.getPoints() == achievementProgress.getCurrentPoints()) {
            achievementService.giveAchievement(achievementDto, event.getMentorId());
            log.info("User with ID {} has now received the {} achievement.", event.getMentorId(), achievementDto.getTitle());
        }
        log.info("Finished handle event for user with ID {}", event.getMentorId());
    }
}
