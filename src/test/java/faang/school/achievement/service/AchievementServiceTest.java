package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementCacheDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
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
    private UserAchievementRepository userAchievementRepository;

    @MockBean
    private AchievementProgressRepository achievementProgressRepository;

    @MockBean
    private AchievementMapper achievementMapper;

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private CacheManager cacheManager;

    private Cache cache;

    private String achievementName;
    private Achievement achievement;
    private AchievementCacheDto dto;
    private long userId;
    private long achievementId;

    @BeforeEach
    void setUp() {
        achievementName = "expert";
        userId = 1L;
        achievementId = 1L;

        cache = cacheManager.getCache("achievements");
        if (cache != null) {
            cache.clear();
        }

        achievement = Achievement.builder()
                .id(achievementId)
                .title("EXPERT")
                .description("description")
                .rarity(Rarity.UNCOMMON)
                .userAchievements(null)
                .progresses(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        dto = AchievementCacheDto.builder()
                .id(achievementId)
                .title("EXPERT")
                .description("description")
                .rarity(Rarity.UNCOMMON)
                .createdAt(achievement.getCreatedAt())
                .updatedAt(achievement.getUpdatedAt())
                .build();
    }

    @Test
    @DisplayName("Test get achievement dto by name: first call: returns dto and caches result")
    void testGetAchievementByTitleFirstCall() {
        when(achievementRepository.findByTitle(achievementName.toUpperCase())).thenReturn(Optional.of(achievement));
        when(achievementMapper.toDto(achievement)).thenReturn(dto);

        AchievementCacheDto result = achievementService.getAchievementByTitle(achievementName);

        verify(achievementRepository, times(1)).findByTitle(achievementName.toUpperCase());
        verify(achievementMapper, times(1)).toDto(achievement);

        assertNotNull(result);
        assertEquals(result, dto);

        cache = cacheManager.getCache("achievements");
        assertNotNull(cache);

        AchievementCacheDto cachedResult = cache.get(achievementName.toUpperCase(), AchievementCacheDto.class);
        assertNotNull(cachedResult);
        assertEquals(dto, cachedResult);
    }

    @Test
    @DisplayName("Test get achievement dto by name: two calls: returns dto and caches result")
    void testGetAchievementDtoByNameFromCacheTwoCalls() {
        when(achievementRepository.findByTitle(achievementName.toUpperCase())).thenReturn(Optional.of(achievement));
        when(achievementMapper.toDto(achievement)).thenReturn(dto);

        AchievementCacheDto firstCall = achievementService.getAchievementByTitle(achievementName);
        AchievementCacheDto secondCall = achievementService.getAchievementByTitle(achievementName);

        verify(achievementRepository, times(1)).findByTitle(achievementName.toUpperCase());
        verify(achievementMapper, times(1)).toDto(achievement);

        assertNotNull(firstCall);
        assertNotNull(secondCall);
        assertEquals(firstCall, dto);

        cache = cacheManager.getCache("achievements");
        assertNotNull(cache);

        AchievementCacheDto cachedResult = cache.get(achievementName.toUpperCase(), AchievementCacheDto.class);
        assertNotNull(cachedResult);
        assertEquals(dto, cachedResult);
    }

    @Test
    @DisplayName("Get achievement dto from cache: fail: wrong name")
    void testGetAchievementDtoFromCacheFail_InvalidName() {
        String name = "fake";
        when(achievementRepository.findByTitle(name.toUpperCase())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> achievementService.getAchievementByTitle(name));
        assertEquals("Achievement not found", ex.getMessage());

        verify(achievementRepository, times(1)).findByTitle(name.toUpperCase());
        verify(achievementMapper, never()).toDto(achievement);
    }

    @Test
    @DisplayName("Test hasAchievement: returns true when achievement exists")
    void testHasAchievementReturnsTrue() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(true);

        boolean result = achievementService.hasAchievement(userId, achievementId);

        assertTrue(result);
        verify(userAchievementRepository, times(1)).existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    @DisplayName("Test hasAchievement: returns false when achievement does not exist")
    void testHasAchievementReturnsFalse() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(false);

        boolean result = achievementService.hasAchievement(userId, achievementId);

        assertFalse(result);
        verify(userAchievementRepository, times(1)).existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    @DisplayName("Test createProgress: calls repository method")
    void testCreateProgressCallsRepository() {
        doNothing().when(achievementProgressRepository).createProgressIfNecessary(userId, achievementId);

        achievementService.createProgress(userId, achievementId);

        verify(achievementProgressRepository, times(1)).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    @DisplayName("Test getProgress: returns progress when it exists")
    void testGetProgressReturnsProgress() {
        AchievementProgress progress = new AchievementProgress();
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.of(progress));

        AchievementProgress result = achievementService.getProgress(userId, achievementId);

        assertNotNull(result);
        assertEquals(progress, result);
        verify(achievementProgressRepository, times(1)).findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    @DisplayName("Test getProgress: throws exception when progress does not exist")
    void testGetProgressThrowsException() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> achievementService.getProgress(userId, achievementId));
        assertEquals("Achievement with id 1 does not exist for user with id 1", ex.getMessage());

        verify(achievementProgressRepository, times(1)).findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    @DisplayName("Test giveAchievement: saves user achievement when achievement exists")
    void testGiveAchievementSavesUserAchievement() {
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));

        achievementService.giveAchievement(userId, achievementId);

        verify(achievementRepository, times(1)).findById(achievementId);
        verify(userAchievementRepository, times(1)).save(any(UserAchievement.class));
    }

    @Test
    @DisplayName("Test giveAchievement: throws exception when achievement does not exist")
    void testGiveAchievementThrowsException() {
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> achievementService.giveAchievement(userId, achievementId));
        assertEquals("Achievement with id 1 does not exist", ex.getMessage());

        verify(achievementRepository, times(1)).findById(achievementId);
        verify(userAchievementRepository, never()).save(any(UserAchievement.class));
    }

    @Test
    @DisplayName("Test saveProgress: calls repository method")
    void testSaveProgressCallsRepository() {
        AchievementProgress progress = new AchievementProgress();
        when(achievementProgressRepository.save(progress)).thenReturn(progress);

        achievementService.saveProgress(progress);

        verify(achievementProgressRepository, times(1)).save(progress);
    }
}