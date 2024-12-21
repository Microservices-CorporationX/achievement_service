package faang.school.achievement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
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

    private AchievementDto achievementDto;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(achievementController).build();
        achievementDto = AchievementDto.builder()
                .userId(1L)
                .achievementId(1L)
                .build();
    }

    @Test
    @DisplayName("testPublishAchievementEvent")
    void testPublishAchievementEvent() throws Exception {
        String publishAchievementEventUrl = "/achievements/publish";
        doNothing().when(achievementService)
                .publishAchievementEvent(achievementDto.getUserId(), achievementDto.getAchievementId());

        mockMvc.perform(post(publishAchievementEventUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(achievementDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
