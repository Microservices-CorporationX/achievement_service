package faang.school.achievement.handler.comment.expert;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.comment.CommentEvent;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpertAchievementHandlerTest {

    @Mock
    private AchievementCache achievementCache;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private ExpertAchievementHandler expertAchievementHandler;

    @Test
    public void testHandleEventIfAchievementNull() {
        CommentEvent event = prepareEvent();
        when(achievementCache.get(anyString())).thenReturn(null);

        AchievementNotFoundException exception = assertThrows(AchievementNotFoundException.class,
                () -> expertAchievementHandler.handleEvent(event));

        assertEquals("Failed to get achievement from cache.", exception.getMessage());
    }

    @Test
    public void testHandleEventIfUserAlreadyHasAchievement() {
        CommentEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(10);
        when(achievementCache.get(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getAuthorId(), 1L)).thenReturn(true);

        expertAchievementHandler.handleEvent(event);


        verify(achievementService, never()).createProgressIfNecessary(event.getAuthorId(), achievement.getId());
        verify(achievementService, never()).saveProgress(achievementProgress);
        verify(achievementService, never()).giveAchievement(achievement, event.getAuthorId());
    }

    @Test
    public void testHandleEventIfUserGivenAchievement() {
        CommentEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(9);
        when(achievementCache.get(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getAuthorId(), 1L)).thenReturn(false);
        when(achievementService.getProgress(event.getAuthorId(), achievement.getId())).thenReturn(achievementProgress);

        expertAchievementHandler.handleEvent(event);

        verify(achievementService).createProgressIfNecessary(event.getAuthorId(), achievement.getId());
        verify(achievementService).saveProgress(achievementProgress);
        verify(achievementService).giveAchievement(achievement, event.getAuthorId());
    }

    @Test
    public void testHandleEventIfUserNotGivenAchievement() {
        CommentEvent event = prepareEvent();
        AchievementDto achievement = prepareAchievementDto();
        AchievementProgress achievementProgress = prepareAchievementProgress(8);
        when(achievementCache.get(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(event.getAuthorId(), 1L)).thenReturn(false);
        when(achievementService.getProgress(event.getAuthorId(), achievement.getId())).thenReturn(achievementProgress);

        expertAchievementHandler.handleEvent(event);

        verify(achievementService).createProgressIfNecessary(event.getAuthorId(), achievement.getId());
        verify(achievementService).saveProgress(achievementProgress);
        verify(achievementService, never()).giveAchievement(achievement, event.getAuthorId());
    }

    private CommentEvent prepareEvent() {
        return CommentEvent.builder()
                .authorId(1L)
                .commentId(2L)
                .postId(3L)
                .content("1234")
                .build();
    }

    private AchievementDto prepareAchievementDto() {
        return AchievementDto.builder()
                .id(1L)
                .points(10L)
                .build();
    }

    private AchievementProgress prepareAchievementProgress(long currentPoints) {
        return AchievementProgress.builder()
                .id(2L)
                .currentPoints(currentPoints)
                .build();
    }
}
