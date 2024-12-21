package faang.school.achievement.handler.mentorship;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.event.MentorshipStartEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.service.AchievementService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class SenseiAchievementHandler implements MentorshipStartEventHandler {
    private final AchievementCache achievementCache;
    private final AchievementService achievementService;
    private final AchievementProgressRepository achievementProgressRepository;
    @Value("${achievement-title.sensei}")
    private String achievementTitle;

    @Async
    @Override
    public void handleEvent(MentorshipStartEvent event) {
        Achievement achievement = achievementCache.getAchievement(achievementTitle)
                .orElseThrow(() -> new IllegalArgumentException("Wrong achievement title"));
        Long mentorId = event.getMentorId();
        Long achievementId = achievement.getId();

        if (!achievementService.hasAchievement(mentorId, achievementId)) {
            achievementService.createProgressIfNecessary(mentorId, achievementId);
            AchievementProgress progress = achievementService.getProgress(mentorId, achievementId);
            progress.increment();
            achievementProgressRepository.save(progress);

            if (progress.getCurrentPoints() >= achievement.getPoints()) {
                achievementService.giveAchievement(mentorId, achievement);
            }
        }
    }
}