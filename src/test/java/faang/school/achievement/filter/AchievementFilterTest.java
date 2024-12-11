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
        Stream<AchievementDto> result = getResult(new TitleFilter());

        Assertions.assertEquals(1, result.toList().size());
    }

    @Test
    public void testDescriptionFilter() {
        Stream<AchievementDto> result = getResult(new DescriptionFilter());

        Assertions.assertEquals(1, result.toList().size());
    }

    @Test
    public void testRarityFilter() {
        Stream<AchievementDto> result = getResult(new RarityFilter());

        Assertions.assertEquals(2, result.toList().size());
    }

    private Stream<AchievementDto> getResult(AchievementFilter filter) {
        AchievementDto filters = setUpAchievementDto();

        AchievementDto first = AchievementDto.builder()
                .title("title")
                .description("no")
                .rarity(Rarity.LEGENDARY).build();

        AchievementDto second = AchievementDto.builder()
                .title("name")
                .description("description")
                .rarity(Rarity.LEGENDARY).build();

        AchievementDto third = AchievementDto.builder()
                .title("third")
                .description("third")
                .rarity(Rarity.COMMON).build();

        Stream<AchievementDto> achievements = Stream.of(first, second, third);
        return filter.apply(achievements, filters);
    }

    private AchievementDto setUpAchievementDto() {
        return AchievementDto.builder()
                .title("title")
                .description("description")
                .rarity(Rarity.LEGENDARY)
                .build();
    }
}
