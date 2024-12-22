package faang.school.achievement.filter;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;

import java.util.List;

public interface AchievementFilter {
    boolean isApplicable(AchievementFilterDto filterDto);

    List<Achievement> apply(List<Achievement> achievements, AchievementFilterDto filterDto);
}
