package faang.school.achievement.filter.achievement;

import faang.school.achievement.dto.achievement.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AchievementRarityFilter implements AchievementFilter {
    @Override
    public boolean isApplicable(AchievementFilterDto achievementFilterDto) {
        return achievementFilterDto.rarity() != null;
    }

    @Override
    public Stream<Achievement> apply(Stream<Achievement> achievements, AchievementFilterDto achievementFilterDto) {
        return achievements.filter(achievement -> achievement.getRarity().equals(achievementFilterDto.rarity()));
    }
}
