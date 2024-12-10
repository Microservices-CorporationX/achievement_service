package faang.school.achievement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AchievementV1ControllerTest {

    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    private MockMvc mockMvc;
    @InjectMocks
    private AchievementV1Controller achievementV1Controller;
    @Mock
    private AchievementService achievementService;
    private AchievementDto firstAchievement;
    private AchievementDto secondAchievement;
    private AchievementDto thirdAchievement;
    private List<AchievementDto> achievements;
    private long userId;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(achievementV1Controller).build();

        firstAchievement = AchievementDto.builder()
                .id(1L)
                .title("some title")
                .rarity(Rarity.COMMON)
                .build();
        secondAchievement = AchievementDto.builder()
                .id(2L)
                .title("some title")
                .rarity(Rarity.RARE)
                .build();
        thirdAchievement = AchievementDto.builder()
                .id(3L)
                .title("some title")
                .rarity(Rarity.LEGENDARY)
                .build();
        achievements = List.of(
                firstAchievement,
                secondAchievement,
                thirdAchievement
        );

        userId = 5L;
    }

    @Test
    public void testGetAllPossibleAchievements() throws Exception {
        // arrange
        AchievementFilterDto filterDto = AchievementFilterDto.builder()
                .description("some title")
                .build();

        when(achievementService.getAllPossibleAchievements(filterDto))
                .thenReturn(achievements);
        String requestBody = ow.writeValueAsString(filterDto);

        // act and assert
        mockMvc.perform(post("/api/v1/achievements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("some title")))
                .andExpect(jsonPath("$[0].rarity", is("COMMON")))
                .andExpect(jsonPath("$[1].title", is("some title")))
                .andExpect(jsonPath("$[1].rarity", is("RARE")))
                .andExpect(jsonPath("$[2].title", is("some title")))
                .andExpect(jsonPath("$[2].rarity", is("LEGENDARY")));
    }

    @Test
    public void testGetAllUserAchievements() throws Exception {
        // arrange
        when(achievementService.getAllUserAchievements(userId))
                .thenReturn(achievements);

        // act and assert
        mockMvc.perform(get("/api/v1/achievements/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("some title")))
                .andExpect(jsonPath("$[0].rarity", is("COMMON")))
                .andExpect(jsonPath("$[1].title", is("some title")))
                .andExpect(jsonPath("$[1].rarity", is("RARE")))
                .andExpect(jsonPath("$[2].title", is("some title")))
                .andExpect(jsonPath("$[2].rarity", is("LEGENDARY")));
    }

    @Test
    public void testGetUnfinishedUserAchievements() throws Exception {
        // arrange
        AchievementProgressDto firstAchievement = AchievementProgressDto.builder()
                .id(1L)
                .achievementId(1L)
                .title("some title")
                .rarity(Rarity.COMMON)
                .currentPoints(20L)
                .points(50L)
                .build();
        AchievementProgressDto secondAchievement = AchievementProgressDto.builder()
                .id(2L)
                .achievementId(2L)
                .title("some title")
                .rarity(Rarity.RARE)
                .currentPoints(30L)
                .points(100L)
                .build();
        AchievementProgressDto thirdAchievement = AchievementProgressDto.builder()
                .id(3L)
                .achievementId(3L)
                .title("some title")
                .rarity(Rarity.LEGENDARY)
                .currentPoints(50L)
                .points(80L)
                .build();
        List<AchievementProgressDto> achievements = List.of(
                firstAchievement,
                secondAchievement,
                thirdAchievement
        );

        when(achievementService.getUnfinishedUserAchievements(userId))
                .thenReturn(achievements);

        // act and assert
        mockMvc.perform(get("/api/v1/achievements/user/{userId}/unfinished", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("some title")))
                .andExpect(jsonPath("$[0].rarity", is("COMMON")))
                .andExpect(jsonPath("$[0].currentPoints", is(20)))
                .andExpect(jsonPath("$[0].points", is(50)))
                .andExpect(jsonPath("$[1].title", is("some title")))
                .andExpect(jsonPath("$[1].rarity", is("RARE")))
                .andExpect(jsonPath("$[1].currentPoints", is(30)))
                .andExpect(jsonPath("$[1].points", is(100)))
                .andExpect(jsonPath("$[2].title", is("some title")))
                .andExpect(jsonPath("$[2].rarity", is("LEGENDARY")))
                .andExpect(jsonPath("$[2].currentPoints", is(50)))
                .andExpect(jsonPath("$[2].points", is(80)));
    }

    @Test
    public void testGetAchievement() throws Exception {
        // arrange
        when(achievementService.getAchievement(1L))
                .thenReturn(firstAchievement);

        // act and assert
        mockMvc.perform(get("/api/v1/achievements/{achievementId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("some title")))
                .andExpect(jsonPath("$.rarity", is("COMMON")));
    }
}
