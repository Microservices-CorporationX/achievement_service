package faang.school.achievement.handler;

import faang.school.achievement.dto.AchievementCacheDto;
import faang.school.achievement.enums.AchievementTitle;
import faang.school.achievement.event.RecommendationEvent;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementProgressService;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.UserAchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NiceGuyAchievementHandlerTest {

    @Mock
    private AchievementService achievementService;

    @Mock
    private UserAchievementService userAchievementService;

    @Mock
    private AchievementProgressService achievementProgressService;

    @Mock
    private AchievementMapper achievementMapper;

    @InjectMocks
    private NiceGuyAchievementHandler niceGuyAchievementHandler;

    private final String title = AchievementTitle.NICE_GUY.getValue();

    @Test
    void testHandleEventGiveAchievementFailed() {
        var event = setRecommendationEvent();
        var achievementCacheDto = setAchievementCacheDto();
        var progress = setAchievementProgress();
        var achievement = setAchievement();
        long userId = 1L;
        long achievementId = 1L;

        when(achievementService.getAchievementByTitle(title)).thenReturn(achievementCacheDto);
        when(userAchievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementProgressService.getProgress(userId, achievementId)).thenReturn(progress);

        progress.setCurrentPoints(achievementCacheDto.getPoints() - 1);

        niceGuyAchievementHandler.handleEvent(event);

        verify(achievementProgressService).createProgressIfNecessary(userId, achievementId);
        verify(achievementProgressService).getProgress(userId, achievementId);
        verify(achievementProgressService).incrementUserAchievementProgress(progress.getId());
        verify(userAchievementService, never()).giveAchievement(userId, achievement);
    }

    @Test
    void testHandleEventGiveAchievementSuccess() {
        var event = setRecommendationEvent();
        var achievementCacheDto = setAchievementCacheDto();
        var progress = setAchievementProgress();
        var achievement = setAchievement();
        long userId = 1L;
        long achievementId = 1L;

        when(achievementService.getAchievementByTitle(title)).thenReturn(achievementCacheDto);
        when(userAchievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementProgressService.getProgress(userId, achievementId)).thenReturn(progress);
        when(achievementMapper.toEntity(achievementCacheDto)).thenReturn(achievement);
        progress.setCurrentPoints(achievementCacheDto.getPoints());

        niceGuyAchievementHandler.handleEvent(event);

        verify(achievementProgressService).createProgressIfNecessary(userId, achievementId);
        verify(achievementProgressService).getProgress(userId, achievementId);
        verify(userAchievementService).giveAchievement(anyLong(), any(Achievement.class));
    }

    private RecommendationEvent setRecommendationEvent() {
        return new RecommendationEvent(1L, 1L, 1L);
    }

    private Achievement setAchievement() {
        return Achievement.builder()
                .title(title)
                .id(1L)
                .build();
    }

    private AchievementCacheDto setAchievementCacheDto() {
        return AchievementCacheDto.builder()
                .id(1L)
                .points(9)
                .build();
    }

    private AchievementProgress setAchievementProgress() {
        return AchievementProgress.builder()
                .id(1L)
                .currentPoints(8)
                .build();
    }
}

