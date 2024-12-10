package faang.school.achievement.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.testcontainers.RedisContainer;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.model.Rarity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class AchievementControllerIT {

    @Autowired
    protected MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Container
    public static PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:13.6");
    @Container
    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis/redis-stack:latest"))
                    .withExposedPorts(6379)
                    .waitingFor(Wait.forListeningPort())
                    .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*\\n", 1))
                    .withStartupTimeout(Duration.ofMinutes(1));


    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
    }

    @Test
    public void testGetAchievement() throws Exception {

        AchievementDto expectedAchievement = new AchievementDto();
        expectedAchievement.setId(1);
        expectedAchievement.setTitle("COLLECTOR");
        expectedAchievement.setDescription("For 100 goals");
        expectedAchievement.setRarity(Rarity.valueOf("EPIC"));
        expectedAchievement.setPoints(15);

        MvcResult result = mockMvc.perform(
                        get("/achievements/COLLECTOR")
                                .header("x-user-id", 1)
                ).andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        AchievementDto actualAchievement = objectMapper.readValue(responseBody, AchievementDto.class);
        assertEquals(expectedAchievement.getId(), actualAchievement.getId());
        assertEquals(expectedAchievement.getTitle(), actualAchievement.getTitle());
        assertEquals(expectedAchievement.getDescription(), actualAchievement.getDescription());
        assertEquals(expectedAchievement.getRarity(), actualAchievement.getRarity());
        assertEquals(expectedAchievement.getPoints(), actualAchievement.getPoints());
    }

    @Test
    public void testGetAllAchievements() throws Exception {
        int totalAchievements = 8;

        MvcResult result = mockMvc.perform(
                get("/achievements")
                        .header("x-user-id", 1)
        ).andExpect(status().isOk())
        .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<AchievementDto> actualAchievements = objectMapper.readValue(responseBody, new TypeReference<>(){});
        assertEquals(totalAchievements, actualAchievements.size());
    }
}