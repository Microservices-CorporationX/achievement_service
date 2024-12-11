package faang.school.achievement.filter;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.filters.achievement.AchievementFilter;
import faang.school.achievement.filters.achievement.DescriptionFilter;
import faang.school.achievement.filters.achievement.RarityFilter;
import faang.school.achievement.filters.achievement.TitleFilter;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class AchievementFilterTest {

    @Test
    public void testTitleFilter() {
        AchievementFilter filter = new TitleFilter();
        AchievementDto filters = setUpAchievementDto();

        Achievement first = Achievement.builder()
                .title("title")
                .description("no")
                .rarity(Rarity.LEGENDARY).build();

        Achievement second = Achievement.builder()
                .title("name")
                .description("description")
                .rarity(Rarity.LEGENDARY).build();

        Achievement third = Achievement.builder()
                .title("third")
                .description("third")
                .rarity(Rarity.COMMON).build();

        Stream<Achievement> achievements = Stream.of(first, second, third);
        Stream<Achievement> result = filter.apply(achievements, filters);

        Assertions.assertEquals(1, result.toList().size());
    }

    @Test
    public void testDescriptionFilter() {
        AchievementFilter filter = new DescriptionFilter();
        AchievementDto filters = setUpAchievementDto();

        Achievement first = Achievement.builder()
                .title("title")
                .description("no")
                .rarity(Rarity.LEGENDARY).build();

        Achievement second = Achievement.builder()
                .title("name")
                .description("description")
                .rarity(Rarity.LEGENDARY).build();

        Achievement third = Achievement.builder()
                .title("third")
                .description("third")
                .rarity(Rarity.COMMON).build();

        Stream<Achievement> achievements = Stream.of(first, second, third);
        Stream<Achievement> result = filter.apply(achievements, filters);

        Assertions.assertEquals(1, result.toList().size());
    }

    @Test
    public void testRarityFilter() {
        AchievementFilter filter = new RarityFilter();
        AchievementDto filters = setUpAchievementDto();

        Achievement first = Achievement.builder()
                .title("title")
                .description("no")
                .rarity(Rarity.LEGENDARY).build();

        Achievement second = Achievement.builder()
                .title("name")
                .description("description")
                .rarity(Rarity.LEGENDARY).build();

        Achievement third = Achievement.builder()
                .title("third")
                .description("third")
                .rarity(Rarity.COMMON).build();

        Stream<Achievement> achievements = Stream.of(first, second, third);
        Stream<Achievement> result = filter.apply(achievements, filters);

        Assertions.assertEquals(2, result.toList().size());
    }

    private AchievementDto setUpAchievementDto() {
        return AchievementDto.builder()
                .title("title")
                .description("description")
                .rarity(Rarity.LEGENDARY)
                .build();
    }
}
