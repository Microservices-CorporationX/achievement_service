package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementCacheDto;
import faang.school.achievement.enums.AchievementTitle;
import faang.school.achievement.event.RecommendationEvent;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementProgressService achievementProgressService;

    @Mock
    private UserAchievementService userAchievementService;

    @Mock
    private AchievementMapper achievementMapper;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testCreateProgressAndIncrementPointsWhenReceiverDoesNotHaveAchievement() {
        RecommendationEvent event = setRecommendationEvent();
        AchievementCacheDto achievementCacheDto = setAchievementCacheDto();
        AchievementProgress progress = setAchievementProgress();

        when(achievementService.getAchievementByTitle(AchievementTitle.NICE_GUY.name())).thenReturn(achievementCacheDto);
        when(userAchievementService.hasAchievement(event.receiverId(), achievementCacheDto.getId())).thenReturn(false);
        when(achievementProgressService.getProgress(event.receiverId(), achievementCacheDto.getId())).thenReturn(progress);

        transactionService.handle(event);

        verify(achievementProgressService, times(1)).createProgressIfNecessary(event.receiverId(), achievementCacheDto.getId());
        verify(achievementProgressService, times(1)).incrementUserAchievementProgress(progress.getId());
        verify(userAchievementService, never()).giveAchievement(anyLong(), any(Achievement.class));
    }

    @Test
    void testGiveAchievementWhenProgressEqualsRequiredPoints() {
        RecommendationEvent event = setRecommendationEvent();

        AchievementCacheDto achievementCacheDto = new AchievementCacheDto();
        achievementCacheDto.setId(1L);
        achievementCacheDto.setPoints(5);

        AchievementProgress progress = new AchievementProgress();
        progress.setId(10L);
        progress.setCurrentPoints(5);

        Achievement achievement = new Achievement();

        when(achievementService.getAchievementByTitle(AchievementTitle.NICE_GUY.name())).thenReturn(achievementCacheDto);
        when(userAchievementService.hasAchievement(event.receiverId(), achievementCacheDto.getId())).thenReturn(false);
        when(achievementProgressService.getProgress(event.receiverId(), achievementCacheDto.getId())).thenReturn(progress);
        when(achievementMapper.toEntity(achievementCacheDto)).thenReturn(achievement);

        transactionService.handle(event);

        verify(achievementProgressService, never()).incrementUserAchievementProgress(anyLong());
        verify(userAchievementService, times(1)).giveAchievement(event.receiverId(), achievement);
    }

    @Test
    void shouldDoNothingWhenUserAlreadyHasAchievement() {
        RecommendationEvent event = setRecommendationEvent();
        AchievementCacheDto achievementCacheDto = setAchievementCacheDto();
        when(achievementService.getAchievementByTitle(AchievementTitle.NICE_GUY.name())).thenReturn(achievementCacheDto);
        when(userAchievementService.hasAchievement(event.receiverId(), achievementCacheDto.getId())).thenReturn(true);

        transactionService.handle(event);

        verify(achievementProgressService, never()).createProgressIfNecessary(anyLong(), anyLong());
        verify(achievementProgressService, never()).getProgress(anyLong(), anyLong());
        verify(achievementProgressService, never()).incrementUserAchievementProgress(anyLong());
        verify(userAchievementService, never()).giveAchievement(anyLong(), any(Achievement.class));
    }


    private RecommendationEvent setRecommendationEvent() {
        return new RecommendationEvent(1L, 1L, 1L);
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