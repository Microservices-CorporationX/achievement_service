package faang.school.achievement.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "achievement-titles.sensei")
@Getter
@Setter
public class SenseiAchievementConfig {
    private String title;
    private int requiredPoints;
}
