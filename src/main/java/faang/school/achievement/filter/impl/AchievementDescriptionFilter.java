package faang.school.achievement.filter.impl;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.filter.AchievementFilter;
import faang.school.achievement.model.Achievement;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AchievementDescriptionFilter implements AchievementFilter {

    @Override
    public boolean isApplicable(AchievementFilterDto filterDto) {
        return filterDto.description() != null;
    }

    @Override
    public List<Achievement> apply(List<Achievement> achievements, AchievementFilterDto filterDto) {
        return achievements.stream()
                .filter(achievement -> achievement.getDescription().contains(filterDto.description()))
                .toList();
    }
}
