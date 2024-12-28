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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SenseyAchievementHandlerTest {
    @InjectMocks
    private SenseyAchievementHandler senseyAchievementHandler;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private AchievementService achievementService;
    @Mock
    private UserContext userContext;
    @Mock
    private SenseyAchievementHandlerValidator validator;
    private MentorshipAcceptedEvent event;
    @Mock
    private UserDto userDto;
    @Mock
    private Achievement achievement;
    @Mock
    private AchievementProgress achievementProgress;

    @BeforeEach
    void setUp() {
        event = new MentorshipAcceptedEvent();

        event.setUserContextId(1111);
        event.setRequesterUserId(13);
        event.setRequesterUserId(14);
    }

    @Test
    void testStartHandlingThrownException() {
        Mockito.doThrow(IllegalArgumentException.class).when(validator).validateMentorshipStartEvent(null);

        assertThrows(IllegalArgumentException.class,
                () -> senseyAchievementHandler.startHandling(null));
    }

    @Test
    void testAchievementIsGranted() {
        setUpForMainTest();

        senseyAchievementHandler.startHandling(event);

        verify(userContext).setUserId(event.getUserContextId());
        verify(userServiceClient).getUser(event.getReceiverUserId());
        verify(achievementService).getAchievementByTitleWithOutUserAndProgress("SENSEI");
        verify(achievement).getId();
        verify(achievementService).hasAchievement(event.getReceiverUserId(), 2L);
        verify(achievementProgress).increment();
        verify(achievementProgress).getCurrentPoints();
        verify(achievement).getPoints();
        verify(userDto).getMenteesIds();

        verify(achievementService).createNewUserAchievement(any(UserAchievement.class));
    }

    @Test
    void testAchievementIsNotGranted() {
        setUpForMainTest();

        when(userDto.getMenteesIds()).thenReturn(new ArrayList<>(Arrays.asList(1L, 2L)));

        senseyAchievementHandler.startHandling(event);

        verify(userContext).setUserId(event.getUserContextId());
        verify(userServiceClient).getUser(event.getReceiverUserId());
        verify(achievementService).getAchievementByTitleWithOutUserAndProgress("SENSEI");
        verify(achievement).getId();
        verify(achievementService).hasAchievement(event.getReceiverUserId(), 2L);
        verify(achievementProgress).increment();
        verify(achievementProgress).getCurrentPoints();
        verify(achievement).getPoints();
        verify(userDto).getMenteesIds();

        verify(achievementService, never()).createNewUserAchievement(any());
    }

    private void setUpForMainTest() {
        when(userServiceClient.getUser(event.getReceiverUserId())).thenReturn(userDto);
        when(achievementService.getAchievementByTitleWithOutUserAndProgress("SENSEI")).thenReturn(achievement);
        when(achievement.getId()).thenReturn(2L);
        when(achievementService.hasAchievement(event.getReceiverUserId(), 2L)).thenReturn(false);
        when(achievementService.getProgress(event.getReceiverUserId(), 2L)).thenReturn(achievementProgress);
        when(achievementProgress.getCurrentPoints()).thenReturn(30L);
        when(achievement.getPoints()).thenReturn(30L);
        when(userDto.getMenteesIds()).thenReturn(new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L,
                11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L, 23L, 24L, 25L, 26L, 27L, 28L, 29L, 30L)));
    }
}