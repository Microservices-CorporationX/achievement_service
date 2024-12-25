package faang.school.achievement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.AchievementCacheDto;
import faang.school.achievement.exception.GlobalExceptionHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.service.AchievementService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AchievementControllerTest {
    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private AchievementController achievementController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private AchievementCacheDto achievementCacheDto;

    private final String PROCESS_ACHIEVEMENT_URL = "/achievements/{achievementId}/user/{userId}";

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(achievementController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        achievementCacheDto = AchievementCacheDto.builder()
                .id(1L)
                .title("Test Achievement")
                .description("This is a test achievement")
                .rarity(Rarity.COMMON)
                .build();
    }

    @Test
    @DisplayName("Test get achievement success")
    public void testGetAchievementSuccess() throws Exception {
        when(achievementService.getAchievementByTitle(achievementCacheDto.getTitle())).thenReturn(achievementCacheDto);

        mockMvc.perform(get("/achievements")
                        .param("title", achievementCacheDto.getTitle()))
                .andExpect(status().isOk())
                .andExpect(content().string(achievementCacheDto.getTitle()));
    }

    @Test
    @DisplayName("Test get achievement not found")
    public void testGetAchievementNotFound() throws Exception {
        String title = "Nonexistent Achievement";
        when(achievementService.getAchievementByTitle(title)).thenThrow(new EntityNotFoundException("Achievement not found"));

        mockMvc.perform(get("/achievements")
                        .param("title", title))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Achievement not found"));
    }

    @Test
    @DisplayName("Test process achievement for user: success")
    void testProcessAchievementForUser_Success() throws Exception {
        long userId = 1L;
        long achievementId = 123L;

        Achievement achievement = new Achievement();
        achievement.setId(achievementId);
        achievement.setTitle("Expert");
        achievement.setDescription("Complete the expert level");

        doNothing().when(achievementService).processAchievementForUser(userId, achievementId);

        mockMvc.perform(post(PROCESS_ACHIEVEMENT_URL, achievementId, userId))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("Test process achievement for user: achievement not found")
    void testProcessAchievementForUser_NotFound() throws Exception {
        long userId = 1L;
        long achievementId = 999L;

        doThrow(new EntityNotFoundException(String.format("Achievement %d not found", achievementId)))
                .when(achievementService).processAchievementForUser(userId, achievementId);


        mockMvc.perform(post(PROCESS_ACHIEVEMENT_URL, achievementId, userId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Achievement 999 not found"));
    }
}
