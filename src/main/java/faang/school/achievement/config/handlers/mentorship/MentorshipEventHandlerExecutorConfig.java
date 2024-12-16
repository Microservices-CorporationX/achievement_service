package faang.school.achievement.config.handlers.mentorship;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class MentorshipEventHandlerExecutorConfig {

    @Bean
    public ExecutorService mentorshipEventPool() {
        return Executors.newCachedThreadPool();
    }
}
