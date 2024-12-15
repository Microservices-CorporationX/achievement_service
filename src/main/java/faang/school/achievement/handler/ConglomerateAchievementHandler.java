package faang.school.achievement.handler;

import faang.school.achievement.dto.achievement.AchievementDto;
import faang.school.achievement.dto.achievement.AchievementProgressDto;
import faang.school.achievement.dto.event.AchievementEventDto;
import faang.school.achievement.service.ConglomerateAchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConglomerateAchievementHandler {

    private final ConglomerateAchievementService service;

    public void handleEvent(AchievementEventDto eventDto) {
        log.info("Handling achievement: {}", eventDto);

        if (eventDto.getUserId() == null || eventDto.getAchievementTitle() == null) {
            log.info("User id or achievement title is missing");
            throw new IllegalStateException("Achievement or user id is null!");
        }

        AchievementDto achievementDto = service.getAchievement(eventDto.getAchievementTitle());

        if (service.hasAchievement(eventDto.getUserId(), achievementDto.getId())){
            log.info("The user {} has already achievement with id {}", eventDto.getUserId(), achievementDto.getId());
        }

        service.createProgressIfNecessary(eventDto.getUserId(), achievementDto.getId());
        log.info("Progress achievement created");

        AchievementProgressDto progressDto = service.getProgress(eventDto.getUserId(), achievementDto.getId());
        log.info("Progress achievement progress: {}", progressDto);

        if (progressDto.getCurrentPoints() < achievementDto.getPoints()) {
            service.incrementProgress(achievementDto.getId());
            log.info("Progress incremented");
        } else {
            service.giveAchievement(eventDto.getUserId(), achievementDto.getTitle());
            log.info("Conglomerate achievement was assigned");
        }
    }
}
