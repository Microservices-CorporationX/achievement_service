package faang.school.achievement.achievementHandler.invitation;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.invitation.InviteSentEvent;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganizerAchievementHandler extends InvitationEventHandler{

    private final AchievementCache cache;
    private final AchievementService achievementService;
    private final AchievementDto organizer = cache.get("ORGANIZER");

    @Override
    public void handleEvent(InviteSentEvent event) {

        Long userId = event.userId();
        Long organizerId = organizer.getId();

        if (!achievementService.hasAchievement(userId, organizerId)) {

            achievementService.createProgressIfNecessary(userId, organizerId);
            AchievementProgress progress = achievementService.getProgress(userId, organizerId);
            progress.increment();

            if (progress.getCurrentPoints() == organizer.getPoints()) {

                achievementService.giveAchievement(organizer, userId);

            }
        }
    }

}
