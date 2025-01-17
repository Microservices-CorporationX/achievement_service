package faang.school.achievement.handler.mentorship;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.event.MentorshipStartEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class SenseiAchievementHandlerTest {
    @Mock
    AchievementCache achievementCache;
    @Mock
    AchievementService achievementService;
    @Mock
    AchievementProgressRepository progressRepository;
    String achievementTitle = "SENSEI";

    SenseiAchievementHandler senseiAchievementHandler;

    @BeforeEach
    public void init(){
        senseiAchievementHandler = new SenseiAchievementHandler(achievementCache, achievementService, progressRepository);
        senseiAchievementHandler.setAchievementTitle(achievementTitle);
    }

    @Test
    public void testIncreaseCurrentPoints() {
        long mentorId = 1L;
        long achievementId = 1L;
        long currentPoints = 19L;
        long points = currentPoints + 1;
        long progressId = 1L;

        MentorshipStartEvent event = mock(MentorshipStartEvent.class);
        Achievement achievement = mock(Achievement.class);
        AchievementProgress progress = mock(AchievementProgress.class);

        when(achievementCache.getAchievement(achievementTitle)).thenReturn(Optional.of(achievement));
        when(event.getMentorId()).thenReturn(mentorId);
        when(achievement.getId()).thenReturn(achievementId);
        when(achievementService.hasAchievement(mentorId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(mentorId, achievementId)).thenReturn(progress);
        when(progress.getCurrentPoints()).thenReturn(currentPoints);
        when(achievement.getPoints()).thenReturn(points);
        when(progress.getId()).thenReturn(progressId);

        senseiAchievementHandler.handleEvent(event);
        verify(progressRepository, times(1)).incrementCurrentPoints(progressId);
        verify(achievementService, times(0)).giveAchievement(mentorId, achievement);
    }

    @Test
    public void testUserHasAchievement(){
        long mentorId = 1L;
        long achievementId = 1L;

        MentorshipStartEvent event = mock(MentorshipStartEvent.class);
        Achievement achievement = mock(Achievement.class);
        AchievementProgress progress = mock(AchievementProgress.class);

        when(achievementCache.getAchievement(achievementTitle)).thenReturn(Optional.of(achievement));
        when(event.getMentorId()).thenReturn(mentorId);
        when(achievement.getId()).thenReturn(achievementId);
        when(achievementService.hasAchievement(mentorId, achievementId)).thenReturn(true);

        senseiAchievementHandler.handleEvent(event);

        verify(achievementService,times(0)).createProgressIfNecessary(mentorId,achievementId);
        verify(achievementService,times(0)).getProgress(mentorId,achievementId);
        verify(achievementService,times(0)).giveAchievement(mentorId,achievement);
        verify(progressRepository,times(0)).save(any());
    }

    @Test
    public void testAchievementGiven() {
        long mentorId = 1L;
        long achievementId = 1L;
        long currentPoints = 19L;
        long points = currentPoints;
        long progressId = 1L;

        MentorshipStartEvent event = mock(MentorshipStartEvent.class);
        Achievement achievement = mock(Achievement.class);
        AchievementProgress progress = mock(AchievementProgress.class);

        when(achievementCache.getAchievement(achievementTitle)).thenReturn(Optional.of(achievement));
        when(event.getMentorId()).thenReturn(mentorId);
        when(achievement.getId()).thenReturn(achievementId);
        when(achievementService.hasAchievement(mentorId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(mentorId, achievementId)).thenReturn(progress);
        when(progress.getCurrentPoints()).thenReturn(currentPoints);
        when(achievement.getPoints()).thenReturn(points);
        when(progress.getId()).thenReturn(progressId);

        senseiAchievementHandler.handleEvent(event);
        verify(progressRepository, times(1)).incrementCurrentPoints(progressId);
        verify(achievementService, times(1)).giveAchievement(mentorId, achievement);
    }
}