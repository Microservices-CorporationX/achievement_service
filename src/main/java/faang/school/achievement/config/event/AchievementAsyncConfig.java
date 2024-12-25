package faang.school.achievement.config.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AchievementAsyncConfig {

    @Bean
    public ExecutorService InvitationEventExecutor() {
        return Executors.newCachedThreadPool();
    }
}
