package faang.school.achievement.controller;

import faang.school.achievement.dto.achievement.AchievementDto;
import faang.school.achievement.dto.achievement.AchievementFilterDto;
import faang.school.achievement.dto.achievement.AchievementProgressDto;
import faang.school.achievement.dto.achievement.UserAchievementDto;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.service.AchievementService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AchievementController.class)
class AchievementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private AchievementController achievementController;

    private AchievementDto achievementDto;
    private UserAchievementDto userAchievementDto;
    private AchievementProgressDto achievementProgressDto;

    @BeforeEach
    void setUp() {
        achievementDto = new AchievementDto(1L, "Title", "Description", Rarity.COMMON, 100);
        userAchievementDto = new UserAchievementDto(1L, 1L, 123L);
        achievementProgressDto = new AchievementProgressDto(1L, 1L, 123L, 50);
    }

    @Test
    @DisplayName("Позитивный тест: получение достижений по фильтру")
    void testGetAchievementsByFilter() throws Exception {
        AchievementFilterDto filterDto = new AchievementFilterDto("Title", "Description", Rarity.COMMON);
        List<AchievementDto> achievementDtos = List.of(achievementDto);

        when(achievementService.getAchievementsByFilter(filterDto)).thenReturn(achievementDtos);

        mockMvc.perform(post("/achievement")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Title\", \"description\": \"Description\", \"rarity\": \"COMMON\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].title").value("Title"))
            .andExpect(jsonPath("$[0].description").value("Description"))
            .andExpect(jsonPath("$[0].rarity").value("COMMON"))
            .andExpect(jsonPath("$[0].points").value(100));

        verify(achievementService, times(1)).getAchievementsByFilter(filterDto);
    }

    @Test
    @DisplayName("Негативный тест: получение достижений по фильтру с неверным форматом данных")
    void testGetAchievementsByFilterWithInvalidData() throws Exception {
        mockMvc.perform(post("/achievement")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"\", \"description\": \"\", \"rarity\": \"INVALID\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Позитивный тест: получение достижений пользователя по ID")
    void testGetAchievementsByUserId() throws Exception {
        List<UserAchievementDto> userAchievementDtos = List.of(userAchievementDto);

        when(achievementService.getAchievementsByUserId()).thenReturn(userAchievementDtos);

        mockMvc.perform(get("/achievement/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].achievementId").value(1))
            .andExpect(jsonPath("$[0].userId").value(123));

        verify(achievementService, times(1)).getAchievementsByUserId();
    }

    @Test
    @DisplayName("Негативный тест: получение достижений пользователя, когда нет достижений")
    void testGetAchievementsByUserIdNoData() throws Exception {
        when(achievementService.getAchievementsByUserId()).thenReturn(List.of());

        mockMvc.perform(get("/achievement/users"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));

        verify(achievementService, times(1)).getAchievementsByUserId();
    }

    @Test
    @DisplayName("Позитивный тест: получение достижения по ID")
    void testGetAchievementById() throws Exception {
        when(achievementService.getAchievementById(1L)).thenReturn(achievementDto);

        mockMvc.perform(get("/achievement/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Title"))
            .andExpect(jsonPath("$.description").value("Description"))
            .andExpect(jsonPath("$.rarity").value("COMMON"))
            .andExpect(jsonPath("$.points").value(100));

        verify(achievementService, times(1)).getAchievementById(1L);
    }

    @Test
    @DisplayName("Негативный тест: получение достижения по ID, когда достижение не найдено")
    void testGetAchievementByIdNotFound() throws Exception {
        when(achievementService.getAchievementById(999L)).thenThrow(new EntityNotFoundException("Achievement not found"));

        mockMvc.perform(get("/achievement/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Achievement not found"));

        verify(achievementService, times(1)).getAchievementById(999L);
    }

    @Test
    @DisplayName("Позитивный тест: получение прогресса достижений пользователя")
    void testGetAchievementProgressByUserId() throws Exception {
        List<AchievementProgressDto> progressDtos = List.of(achievementProgressDto);

        when(achievementService.getAchievementProgressByUserId()).thenReturn(progressDtos);

        mockMvc.perform(get("/achievement/users/progress"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].achievementId").value(1))
            .andExpect(jsonPath("$[0].userId").value(123))
            .andExpect(jsonPath("$[0].currentPoints").value(50));

        verify(achievementService, times(1)).getAchievementProgressByUserId();
    }

    @Test
    @DisplayName("Негативный тест: получение прогресса достижений пользователя, когда прогресс не найден")
    void testGetAchievementProgressByUserIdNotFound() throws Exception {
        when(achievementService.getAchievementProgressByUserId()).thenReturn(List.of());

        mockMvc.perform(get("/achievement/users/progress"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));

        verify(achievementService, times(1)).getAchievementProgressByUserId();
    }
}
