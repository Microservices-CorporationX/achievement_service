package faang.school.achievement.validator.achievement;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AchievementServiceValidatorTest {
    private AchievementServiceValidator achievementServiceValidator;

    @BeforeEach
    void setUp(){
        achievementServiceValidator = new AchievementServiceValidator();
    }

    @Test
    void testCheckIdThrownException(){
        assertThrows(IllegalArgumentException.class,
                ()->achievementServiceValidator.checkId(-2));
    }
    @Test
    void testCheckTitleThrownException(){
        assertThrows(IllegalArgumentException.class,
                ()->achievementServiceValidator.checkTitle(""));
    }
    @Test
    void testCheckUserAchievementThrownException(){
        assertThrows(IllegalArgumentException.class,
                ()->achievementServiceValidator.checkUserAchievement(null));
    }

    @Test
    void testSuccessCheckId(){
        assertDoesNotThrow(()->achievementServiceValidator.checkId(1));
    }
    @Test
    void testSuccessCheckTitle(){
        assertDoesNotThrow(()->achievementServiceValidator.checkTitle("title"));
    }
    @Test
    void testSuccessCheckUserAchievement(){
        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setAchievement(new Achievement());
        userAchievement.setUserId(12);
        userAchievement.setUpdatedAt(LocalDateTime.now());
        userAchievement.setCreatedAt(LocalDateTime.now());

        assertDoesNotThrow(()->achievementServiceValidator.checkUserAchievement(userAchievement));
    }
}