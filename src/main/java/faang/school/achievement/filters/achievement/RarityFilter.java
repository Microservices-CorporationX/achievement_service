package faang.school.achievement.filters.achievement;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.model.Achievement;

import java.util.stream.Stream;

public class RarityFilter implements AchievementFilter{
    @Override
    public boolean isApplicable(AchievementDto filters) {
        return filters.getRarity() != null;
    }

    @Override
    public Stream<Achievement> apply(Stream<Achievement> achievements, AchievementDto filters) {
        return achievements.filter(achievement -> achievement.getRarity().equals(filters.getRarity()));
    }
}
