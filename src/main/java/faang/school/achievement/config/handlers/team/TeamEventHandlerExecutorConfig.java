package faang.school.achievement.config.handlers.team;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class TeamEventHandlerExecutorConfig {

    @Bean
    public ExecutorService teamEventPool() {
        return Executors.newCachedThreadPool();
    }
}
