package faang.school.achievement.filters.achievement;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.model.Achievement;

import java.util.stream.Stream;

public class DescriptionFilter implements AchievementFilter{
    @Override
    public boolean isApplicable(AchievementDto filters) {
        return filters.getDescription() != null;
    }

    @Override
    public Stream<AchievementDto> apply(Stream<AchievementDto> achievements, AchievementDto filters) {
        return achievements
                .filter(achievement -> achievement.getDescription().toLowerCase()
                        .contains(filters.getDescription().toLowerCase()));
    }
}
