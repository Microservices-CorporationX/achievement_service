package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementControllerTest {

    @InjectMocks
    AchievementController achievementController;

    @Mock
    AchievementService achievementService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(achievementController).build();
    }

    @Test
    public void testGetAllAchievements() throws Exception {
        AchievementDto filter = AchievementDto.builder().build();
        List<AchievementDto> expectedAchievements = Arrays.asList(
                AchievementDto.builder().build(), AchievementDto.builder().build()
        );

        when(achievementService.getAll(any(AchievementDto.class))).thenReturn(expectedAchievements);

        mockMvc.perform(post("/api/v1/achievements/all")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(achievementService).getAll(any(AchievementDto.class));
    }

    @Test
    public void testGetAchievementsByUserId() throws Exception {
        Long userId = 1L;
        List<AchievementDto> expectedAchievements = Arrays.asList(
                AchievementDto.builder().build(), AchievementDto.builder().build()
        );

        when(achievementService.getByUserId(userId)).thenReturn(expectedAchievements);

        mockMvc.perform(get("/api/v1/achievements?user={userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(achievementService, times(1)).getByUserId(userId);
    }

    @Test
    public void testGetAchievementById() throws Exception {
        Long achievementId = 1L;
        AchievementDto expectedAchievement = AchievementDto.builder().build();

        when(achievementService.get(achievementId)).thenReturn(expectedAchievement);

        mockMvc.perform(get("/api/v1/achievements/{achievementId}", achievementId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(achievementService, times(1)).get(achievementId);
    }

    @Test
    public void testGetUnearnedAchievementsByUserId() throws Exception {
        Long userId = 1L;
        List<AchievementDto> expectedAchievements = Arrays.asList(
                AchievementDto.builder().build(), AchievementDto.builder().build()
        );

        when(achievementService.getByUserIdUnearned(userId)).thenReturn(expectedAchievements);

        mockMvc.perform(get("/api/v1/achievements/unearned?user={userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(achievementService, times(1)).getByUserIdUnearned(userId);
    }
}
