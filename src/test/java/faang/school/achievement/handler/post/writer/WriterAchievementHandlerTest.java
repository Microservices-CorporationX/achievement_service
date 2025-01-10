package faang.school.achievement.handler.post.writer;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.post.PostEvent;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriterAchievementHandlerTest {

    @Mock
    private AchievementCache achievementCache;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private WriterAchievementHandler writerAchievementHandler;

    private PostEvent postEvent;
    private AchievementDto achievementDto;
    private AchievementProgress achievementProgress;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(writerAchievementHandler, "writerAchievementKey", "WRITER");

        postEvent = PostEvent.builder()
                .userId(1L)
                .postId(1L)
                .build();
        achievementDto = AchievementDto.builder()
                .id(1L)
                .points(100)
                .title("title")
                .build();
        achievementProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(10);
    }

    @Test
    public void testHandlePostEvent_WhenAchievementNull_ThrowAchievementNotFoundException() {
        when(achievementCache.get(anyString())).thenReturn(null);

        assertThrows(AchievementNotFoundException.class, () -> {
            writerAchievementHandler.handleEvent(postEvent);
        });
        verify(achievementCache, times(1))
                .get(anyString());
    }

    @Test
    public void testHandlePostEvent_WhenUserAlreadyHasAchievement_DoNothing() {
        when(achievementCache.get(anyString()))
                .thenReturn(achievementDto);
        when(achievementService.hasAchievement(anyLong(), anyLong()))
                .thenReturn(true);

        writerAchievementHandler.handleEvent(postEvent);

        verify(achievementService, never()).createProgressIfNecessary(anyLong(), anyLong());
    }

    @Test
    public void testHandlePostEvent_WhenNotEnoughPoints_DontGiveAchieve() {
        when(achievementCache.get(anyString()))
                .thenReturn(achievementDto);
        when(achievementService.hasAchievement(anyLong(), anyLong()))
                .thenReturn(false);
        when(achievementService.getProgress(anyLong(), anyLong()))
                .thenReturn(achievementProgress);

        writerAchievementHandler.handleEvent(postEvent);

        verify(achievementService, never()).giveAchievement(any(), anyLong());
    }

    @Test
    public void testHandlePostEvent_WhenNotEnoughPoints_ShouldGiveAchieve() {
        achievementProgress.setCurrentPoints(99);
        when(achievementCache.get(anyString()))
                .thenReturn(achievementDto);
        when(achievementService.hasAchievement(anyLong(), anyLong()))
                .thenReturn(false);
        when(achievementService.getProgress(anyLong(), anyLong()))
                .thenReturn(achievementProgress);

        writerAchievementHandler.handleEvent(postEvent);

        verify(achievementService, times(1))
                .giveAchievement(any(), anyLong());
    }
}