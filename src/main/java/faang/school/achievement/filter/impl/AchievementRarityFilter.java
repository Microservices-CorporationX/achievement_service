package faang.school.achievement.filter.impl;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.filter.AchievementFilter;
import faang.school.achievement.model.Achievement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AchievementRarityFilter implements AchievementFilter {

    @Override
    public boolean isApplicable(AchievementFilterDto filterDto) {
        return filterDto.rarity() != null;
    }

    @Override
    public List<Achievement> apply(List<Achievement> achievements, AchievementFilterDto filterDto) {
        log.debug("Filtering all achievements with rarity = {}", filterDto.rarity());
        return achievements.stream()
                .filter(achievement -> achievement.getRarity().equals(filterDto.rarity()))
                .toList();
    }
}
