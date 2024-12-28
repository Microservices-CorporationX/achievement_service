package faang.school.achievement.handler.mentorship;

import faang.school.achievement.client.UserServiceClient;
import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.user.UserDto;
import faang.school.achievement.event.MentorshipAcceptedEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.achievement.AchievementService;
import faang.school.achievement.validator.achievement.SenseyAchievementHandlerValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Slf4j
public class SenseyAchievementHandler implements MentorshipHandler {
    private final UserServiceClient userServiceClient;
    private final AchievementService achievementService;
    private final UserContext userContext;
    private final SenseyAchievementHandlerValidator validator;

    @Override
    @Async
    @Transactional
    public void startHandling(MentorshipAcceptedEvent mentorshipAcceptedEvent) {
        log.info("validate mentorshipStartEvent");
        validator.validateMentorshipStartEvent(mentorshipAcceptedEvent);

        long mentorId = mentorshipAcceptedEvent.getReceiverUserId();
        userContext.setUserId(mentorshipAcceptedEvent.getUserContextId());

        log.info("getting user dto from user service by mentor id using feign client");
        UserDto userDto = userServiceClient.getUser(mentorId);

        log.info("getting achievement by title from achievement repository");
        Achievement achievement = achievementService.getAchievementByTitleWithOutUserAndProgress("SENSEI");
        long achievementId = achievement.getId();

        log.info("validate that user does not have SENSEI achievement");
        if (!achievementService.hasAchievement(mentorId, achievementId)) {

            log.info("calling getProgress method from achievementService to get achievementProgress (perhaps create progress)");
            AchievementProgress achievementProgress = achievementService.getProgress(mentorId, achievementId);
            achievementProgress.increment();

            log.info("validate progress for enough points and condition");
            if (achievementProgress.getCurrentPoints() >= achievement.getPoints() && userDto.getMenteesIds().size() > 29) {
                UserAchievement userAchievement = new UserAchievement();

                userAchievement.setAchievement(achievement);
                userAchievement.setUserId(mentorId);
                userAchievement.setCreatedAt(LocalDateTime.now());
                userAchievement.setUpdatedAt(LocalDateTime.now());

                log.info("calling createNewUserAchievement from achievementService to give the achievement to the user");
                achievementService.createNewUserAchievement(userAchievement);
            }
        }
    }
}

