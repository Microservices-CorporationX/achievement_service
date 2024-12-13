package faang.school.achievement.validator.achievement;

import faang.school.achievement.event.MentorshipStartEvent;
import faang.school.achievement.handler.mentorship.SenseyAchievementHandler;
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
        MentorshipStartEvent event = new MentorshipStartEvent();
        event.setMenteeId(12);
        event.setMentorId(123);
        event.setUserContextId(1111);

        assertDoesNotThrow(()->senseyAchievementHandlerValidator.validateMentorshipStartEvent(event));
    }
}