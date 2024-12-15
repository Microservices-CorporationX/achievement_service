package faang.school.achievement.validator.achievement;

import faang.school.achievement.event.MentorshipAcceptedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SenseyAchievementHandlerValidatorTest {
    private SenseyAchievementHandlerValidator senseyAchievementHandlerValidator;

    @BeforeEach
    void setUp(){
        senseyAchievementHandlerValidator = new SenseyAchievementHandlerValidator();
    }

    @Test
    void testValidateMentorshipStartEventThrownException(){
        assertThrows(IllegalArgumentException.class,
                ()->senseyAchievementHandlerValidator.validateMentorshipStartEvent(null));
    }

    @Test
    void testSuccessValidateMentorshipStartEvent(){
        MentorshipAcceptedEvent event = new MentorshipAcceptedEvent();
        event.setRequesterUserId(12);
        event.setRequesterUserId(123);
        event.setUserContextId(1111);

        assertDoesNotThrow(()->senseyAchievementHandlerValidator.validateMentorshipStartEvent(event));
    }
}