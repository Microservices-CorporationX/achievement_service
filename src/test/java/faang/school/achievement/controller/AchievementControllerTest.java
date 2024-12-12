package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AchievementControllerTest {

    @Autowired
    private AchievementController userController;

    @Test
    public void loadContext() {
        List<AchievementDto> achievementDtos = userController.getAchievement();
        assertEquals(0, achievementDtos.size());

    }
}
