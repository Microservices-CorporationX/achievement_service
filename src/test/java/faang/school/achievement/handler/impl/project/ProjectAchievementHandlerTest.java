package faang.school.achievement.handler.impl.project;

import faang.school.achievement.event.project.ProjectEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.achievement.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectAchievementHandlerTest {

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementCache achievementCache;

    private EventHandlerForTest eventHandlerForTest;

    @BeforeEach
    void setUp() {
        eventHandlerForTest = new EventHandlerForTest(achievementService, achievementCache);
    }

    @Test
    public void achievementHasAchievementTest() {
        long userId = 1L;
        long achievementId = 2L;
        String achievementName = "achievementName";

        Achievement achievement = Achievement.builder()
                .id(achievementId)
                .build();
        ProjectEvent event = ProjectEvent.builder()
                .authorId(userId)
                .build();

        when(achievementCache.get(achievementName)).thenReturn(achievement);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(true);

        eventHandlerForTest.handleEvent(event);

        verify(achievementCache, times(1)).get(achievementName);
        verify(achievementService, times(1)).hasAchievement(userId, achievementId);
        verify(achievementService, times(0)).
                createProgressIfNecessary(userId, achievementId);
    }

    @Test
    public void achievementProgressNotEnoughTest() {
        long userId = 1L;
        long achievementId = 2L;
        String achievementName = "achievementName";

        Achievement achievement = Achievement.builder()
                .id(achievementId)
                .points(10)
                .build();
        ProjectEvent event = ProjectEvent.builder()
                .authorId(userId)
                .build();

        AchievementProgress achievementProgress = AchievementProgress.builder()
                .currentPoints(5)
                .build();
        AchievementProgress incrementedAchievementProgress = AchievementProgress.builder()
                .currentPoints(5)
                .build();
        incrementedAchievementProgress.increment();

        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievement)
                .userId(userId)
                .build();

        when(achievementCache.get(achievementName)).thenReturn(achievement);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(achievementProgress);

        eventHandlerForTest.handleEvent(event);

        verify(achievementCache, times(1)).get(achievementName);
        verify(achievementService, times(1)).hasAchievement(userId, achievementId);
        verify(achievementService, times(1)).
                createProgressIfNecessary(userId, achievementId);
        verify(achievementService, times(1)).getProgress(userId, achievementId);
        verify(achievementService).saveProgress(incrementedAchievementProgress);
        verify(achievementService,times(0)).giveAchievement(userAchievement);
    }

    @Test
    public void achievementTest() {
        long userId = 1L;
        long achievementId = 2L;
        String achievementName = "achievementName";

        Achievement achievement = Achievement.builder()
                .id(achievementId)
                .points(4)
                .build();
        ProjectEvent event = ProjectEvent.builder()
                .authorId(userId)
                .build();

        AchievementProgress achievementProgress = AchievementProgress.builder()
                .currentPoints(5)
                .build();
        AchievementProgress incrementedAchievementProgress = AchievementProgress.builder()
                .currentPoints(5)
                .build();
        incrementedAchievementProgress.increment();

        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievement)
                .userId(userId)
                .build();

        when(achievementCache.get(achievementName)).thenReturn(achievement);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(achievementProgress);

        eventHandlerForTest.handleEvent(event);

        verify(achievementCache, times(1)).get(achievementName);
        verify(achievementService, times(1)).hasAchievement(userId, achievementId);
        verify(achievementService, times(1)).
                createProgressIfNecessary(userId, achievementId);
        verify(achievementService, times(1)).getProgress(userId, achievementId);
        verify(achievementService).saveProgress(incrementedAchievementProgress);
        verify(achievementService,times(1)).giveAchievement(userAchievement);
    }
}