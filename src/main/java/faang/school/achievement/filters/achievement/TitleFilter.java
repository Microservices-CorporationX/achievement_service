package faang.school.achievement.filters.achievement;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.model.Achievement;

import java.util.stream.Stream;

public class TitleFilter implements AchievementFilter{
    @Override
    public boolean isApplicable(AchievementDto filters) {
        return filters.getTitle() != null;
    }

    @Override
    public Stream<Achievement> apply(Stream<Achievement> achievements, AchievementDto filters) {
        return achievements
                .filter(achievement -> achievement.getTitle().toLowerCase()
                        .contains(filters.getTitle().toLowerCase()));
    }
}
