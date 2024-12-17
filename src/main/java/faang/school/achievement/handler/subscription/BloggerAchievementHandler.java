package faang.school.achievement.handler.subscription;

import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.handler.AbstractAchievementHandler;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.model.AchievementTitle;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class BloggerAchievementHandler extends AbstractAchievementHandler<FollowerEvent> {

    public BloggerAchievementHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    @Async("fixedThreadPool")
    public void handle(FollowerEvent event) {
        handleAchievement(event.getFolloweeId(), AchievementTitle.BLOGGER.name());
    }
}