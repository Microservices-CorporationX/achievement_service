package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementCacheDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;

    @Transactional
    @Cacheable(value = "achievements", key = "#name.toUpperCase()")
    public AchievementCacheDto getAchievementDtoFromCacheByName(String name) {
        Achievement achievement = achievementRepository.findByName(name.toUpperCase()).orElseThrow(() ->
                new EntityNotFoundException("Achievement not found"));
        log.info("Get achievement by name: {}", name);
        return achievementMapper.toDto(achievement);
    }
}