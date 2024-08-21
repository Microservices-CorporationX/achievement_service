package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.exception.BadRequestException;
import faang.school.achievement.filter.achievement.AchievementFilter;
import faang.school.achievement.dto.SortField;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.exception.ResourceNotFoundException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.mapper.UserAchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Evgenii Malkov
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AchievementService {

    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementProgressMapper achievementProgressMapper;
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;
    private final List<AchievementFilter> achievementFilters;
    private final UserAchievementRepository userAchievementRepository;
    private final UserAchievementMapper userAchievementMapper;

    public List<AchievementDto> getAchievements(AchievementFilterDto filterDto) {
        Pageable pageable = preparePageRequest(filterDto.getPage(), filterDto.getSize(), filterDto.getSortField(), filterDto.getDirection());

        Stream<AchievementDto> achievementList = achievementRepository.findAll(pageable).stream().map(achievementMapper::toDto);

        return achievementFilters.stream()
                .filter(filter -> filter.isApplicable(filterDto))
                .flatMap(filter -> filter.apply(achievementList, filterDto))
                .toList();
    }

    public Pageable preparePageRequest(int page, int size, SortField sortField, Sort.Direction direction) {
        Sort sort = Sort.by(SortField.valueOf(sortField.name()).getValue());
        sort = (direction.isAscending()) ? sort.ascending() : sort.descending();
        return PageRequest.of(page, size, sort);
    }

    public AchievementDto getAchievementById(long id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Achievement with id: %s not found", id)));
        return achievementMapper.toDto(achievement);
    }

    public List<UserAchievementDto> getUserAchievements(long userId) {
        List<UserAchievement> achievements = userAchievementRepository.findByUserId(userId);
        return userAchievementMapper.toListDto(achievements);
    }

    public List<AchievementProgressDto> getUserAchievementsInProgress(long userId) {
        List<AchievementProgress> achievements = achievementProgressRepository.findByUserId(userId);
        return achievementProgressMapper.toListDto(achievements);
    }
}
