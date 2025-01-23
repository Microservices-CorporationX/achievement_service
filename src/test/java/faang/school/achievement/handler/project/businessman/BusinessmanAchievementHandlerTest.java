package faang.school.achievement.handler.project.businessman;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.project.ProjectEvent;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.validator.achievement.AchievementValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusinessmanAchievementHandlerTest {
    public static final String BUSINESSMAN_ACHIEVEMENT_TITLE = "BUSINESSMAN";

    @Mock
    private AchievementValidator achievementValidator;
    @InjectMocks
    private BusinessmanAchievementHandler businessmanAchievementHandler;

    @Test
    public void shouldGrantAchievementWhenUserDoesNotHaveIt() {
        ProjectEvent projectEvent = getProjectEvent();
        AchievementDto achievementDto = getAchievementDto();
        AchievementProgress achievementProgress = new AchievementProgress();
        when(achievementValidator.getAndValidateAchievement(BUSINESSMAN_ACHIEVEMENT_TITLE))
                .thenReturn(achievementDto);
        when(achievementValidator.incrementPoints(projectEvent.getOwnerId(), achievementDto.getId()))
                .thenReturn(achievementProgress);

        businessmanAchievementHandler.handleEvent(projectEvent);

        verify(achievementValidator).incrementPoints(projectEvent.getOwnerId(), achievementDto.getId());
        verify(achievementValidator).giveAchievementIfPointsReached(achievementDto, achievementProgress, projectEvent.getOwnerId());
    }

    @Test
    public void shouldSkipGrantingAchievementWhenUserAlreadyHasIt() {
        ProjectEvent projectEvent = getProjectEvent();
        AchievementDto achievementDto = getAchievementDto();
        when(achievementValidator.getAndValidateAchievement(BUSINESSMAN_ACHIEVEMENT_TITLE))
                .thenReturn(achievementDto);
        when(achievementValidator.checkHasAchievement(projectEvent.getOwnerId(), achievementDto.getId()))
                .thenReturn(true);

        businessmanAchievementHandler.handleEvent(projectEvent);

        verify(achievementValidator).getAndValidateAchievement(BUSINESSMAN_ACHIEVEMENT_TITLE);
        verify(achievementValidator).checkHasAchievement(projectEvent.getOwnerId(), achievementDto.getId());
        verifyNoMoreInteractions(achievementValidator);
    }

    private static ProjectEvent getProjectEvent() {
        return ProjectEvent.builder()
                .ownerId(1L)
                .projectId(1L)
                .build();
    }

    private static AchievementDto getAchievementDto() {
        return AchievementDto.builder()
                .id(1L)
                .title(BUSINESSMAN_ACHIEVEMENT_TITLE)
                .build();
    }
}