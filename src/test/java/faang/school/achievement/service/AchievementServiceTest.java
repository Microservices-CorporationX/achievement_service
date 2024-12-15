package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementCacheDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AchievementServiceTest {

    @MockBean
    private AchievementRepository achievementRepository;

    @MockBean
    private AchievementMapper achievementMapper;

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private CacheManager cacheManager;

    private Cache cache;

    private Achievement achievement;
    private AchievementCacheDto dto;

    @BeforeEach
    void setUp() {
        cache = cacheManager.getCache("achievements");
        if (cache != null) {
            cache.clear();
        }

        achievement = Achievement.builder()
                .id(1L)
                .title("EXPERT")
                .description("description")
                .rarity(Rarity.UNCOMMON)
                .userAchievements(null)
                .progresses(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        dto = AchievementCacheDto.builder()
                .id(1L)
                .title("EXPERT")
                .description("description")
                .rarity(Rarity.UNCOMMON)
                .createdAt(achievement.getCreatedAt())
                .updatedAt(achievement.getUpdatedAt())
                .build();
    }

    @Test
    @DisplayName("Test get achievement dto by name: first call: returns dto and caches result")
    void testGetAchievementDtoFromCacheByNameFirstCall() {
        String name = "expert";
        when(achievementRepository.findByName(name.toUpperCase())).thenReturn(Optional.of(achievement));
        when(achievementMapper.toDto(achievement)).thenReturn(dto);

        AchievementCacheDto result = achievementService.getAchievementDtoFromCacheByName(name);

        verify(achievementRepository, times(1)).findByName(name.toUpperCase());
        verify(achievementMapper, times(1)).toDto(achievement);

        assertNotNull(result);
        assertEquals(result, dto);

        cache = cacheManager.getCache("achievements");
        assertNotNull(cache);

        AchievementCacheDto cachedResult = cache.get(name.toUpperCase(), AchievementCacheDto.class);
        assertNotNull(cachedResult);
        assertEquals(dto, cachedResult);
    }

    @Test
    @DisplayName("Test get achievement dto by name: two calls: returns dto and caches result")
    void testGetAchievementDtoByNameFromCacheTwoCalls() {
        String name = "expert";
        when(achievementRepository.findByName(name.toUpperCase())).thenReturn(Optional.of(achievement));
        when(achievementMapper.toDto(achievement)).thenReturn(dto);

        AchievementCacheDto firstCall = achievementService.getAchievementDtoFromCacheByName(name);
        AchievementCacheDto secondCall = achievementService.getAchievementDtoFromCacheByName(name);

        verify(achievementRepository, times(1)).findByName(name.toUpperCase());
        verify(achievementMapper, times(1)).toDto(achievement);

        assertNotNull(firstCall);
        assertNotNull(secondCall);
        assertEquals(firstCall, dto);

        cache = cacheManager.getCache("achievements");
        assertNotNull(cache);

        AchievementCacheDto cachedResult = cache.get(name.toUpperCase(), AchievementCacheDto.class);
        assertNotNull(cachedResult);
        assertEquals(dto, cachedResult);
    }

    @Test
    @DisplayName("Get achievement dto from cache: fail: wrong name")
    void testGetAchievementDtoFromCacheFail_InvalidName() {
        String name = "fake";
        when(achievementRepository.findByName(name.toUpperCase())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> achievementService.getAchievementDtoFromCacheByName(name));
        assertEquals("Achievement not found", ex.getMessage());

        verify(achievementRepository, times(1)).findByName(name.toUpperCase());
        verify(achievementMapper, never()).toDto(achievement);
    }
}