package faang.school.achievement.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "spring.retry")
public class RetryProperties {
    private int maxAttempts;
    private long initialDelay;
    private int multiplier;
    private long maxDelay;
}
