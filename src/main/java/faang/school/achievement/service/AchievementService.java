package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.filter.AchievementFilter;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementMapper achievementMapper;
    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final List<AchievementFilter> achievementFilters;

    public List<AchievementDto> getAllPossibleAchievements(AchievementFilterDto filterDto) {
        return achievementMapper.toAchievementDto(getFilteredAchievements(filterDto));
    }

    public List<AchievementDto> getAllUserAchievements(long userId) {
        List<UserAchievement> userAchievements = userAchievementRepository.findByUserId(userId);
        List<Achievement> achievements = userAchievements.stream()
                .map(UserAchievement::getAchievement)
                .toList();
        return achievementMapper.toAchievementDto(achievements);
    }

    public List<AchievementProgressDto> getUnfinishedUserAchievements(long userId) {
        List<AchievementProgress> unfinishedUserAchievements = achievementProgressRepository.findByUserId(userId);
        return achievementMapper.toAchievementProgressDto(unfinishedUserAchievements);
    }

    public AchievementDto getAchievement(long achievementId) {
        return achievementMapper.toAchievementDto(getAchievementById(achievementId));
    }

    private List<Achievement> getFilteredAchievements(AchievementFilterDto filterDto) {
        return achievementFilters.stream()
                .filter(filter -> filter.isApplicable(filterDto))
                .reduce(
                        achievementRepository.findAll(),
                        (achievements, filter) -> filter.apply(achievements, filterDto),
                        (list1, list2) -> list2
                );
    }

    private Achievement getAchievementById(long achievementId) {
        return achievementRepository.findById(achievementId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Achievement with id %d does not exist", achievementId
                )));
    }
}
