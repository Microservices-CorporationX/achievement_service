package faang.school.achievement.filters.achievement;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.model.Achievement;

import java.util.stream.Stream;

public interface AchievementFilter {

    boolean isApplicable(AchievementDto filters);
    Stream<Achievement> apply(Stream<Achievement> achievements, AchievementDto filters);
}
