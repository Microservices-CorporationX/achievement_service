package faang.school.achievement.filter.impl;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AchievementTitleFilterTest {

    private AchievementTitleFilter achievementTitleFilter;

    @BeforeEach
    void setUp() {
        achievementTitleFilter = new AchievementTitleFilter();
    }

    @Test
    public void testIsApplicable() {
        // arrange
        AchievementFilterDto filterDto = AchievementFilterDto.builder()
                .title("Some title")
                .build();

        // act
        boolean isApplicable = achievementTitleFilter.isApplicable(filterDto);

        // assert
        assertTrue(isApplicable);
    }

    @Test
    public void testIsApplicableReturnsFalse() {
        // arrange
        AchievementFilterDto filterDto = AchievementFilterDto.builder().build();

        // act
        boolean isApplicable = achievementTitleFilter.isApplicable(filterDto);

        // assert
        assertFalse(isApplicable);
    }

    @Test
    public void apply() {
        // arrange
        AchievementFilterDto filterDto = AchievementFilterDto.builder()
                .title("Some title")
                .build();
        Achievement firstAchievement = Achievement.builder()
                .title("Some title")
                .build();
        Achievement secondAchievement = Achievement.builder()
                .title("Another title")
                .build();
        Achievement thirdAchievement = Achievement.builder()
                .title("Some title")
                .build();
        List<Achievement> achievements = List.of(firstAchievement, secondAchievement, thirdAchievement);

        List<Achievement> expected = List.of(firstAchievement, thirdAchievement);

        // act
        List<Achievement> actual = achievementTitleFilter.apply(achievements, filterDto);

        // assert
        assertEquals(expected, actual);
    }

    @Test
    public void applyEmptyFilterAchievementsEmptyList() {
        // arrange
        AchievementFilterDto filterDto = AchievementFilterDto.builder().build();
        List<Achievement> achievements = new ArrayList<>();

        List<Achievement> expected = new ArrayList<>();

        // act
        List<Achievement> actual = achievementTitleFilter.apply(achievements, filterDto);

        // assert
        assertEquals(expected, actual);
    }
}
