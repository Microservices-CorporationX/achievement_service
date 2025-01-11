package faang.school.achievement.handler.project.businessman;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.project.ProjectEvent;
import faang.school.achievement.handler.project.ProjectEventHandler;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.validator.achievement.AchievementValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessmanAchievementHandler extends ProjectEventHandler {
    public static final String BUSINESSMAN_ACHIEVEMENT_TITLE = "BUSINESSMAN";
    private final AchievementValidator achievementValidator;

    @Override
    public void handleEvent(ProjectEvent event) {
        AchievementDto achievementDto = achievementValidator.getAndValidateAchievement(BUSINESSMAN_ACHIEVEMENT_TITLE);
        if (achievementValidator.checkHasAchievement(event.getOwnerId(), achievementDto.getId())) {
            log.debug("The user with ID {} already has the {} achievement.",
                    event.getOwnerId(), achievementDto.getTitle());
            return;
        }
        AchievementProgress achievementProgress = achievementValidator
                .incrementPoints(event.getOwnerId(), achievementDto.getId());
        achievementValidator.giveAchievementIfPointsReached(achievementDto, achievementProgress, event.getOwnerId());
        log.info("Finished handle event for user with ID {}", event.getOwnerId());
    }
}
