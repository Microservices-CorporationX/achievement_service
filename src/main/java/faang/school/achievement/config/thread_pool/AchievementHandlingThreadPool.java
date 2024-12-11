package faang.school.achievement.config.thread_pool;

import faang.school.achievement.properties.AchievementThreadPoolProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@RequiredArgsConstructor
public class AchievementHandlingThreadPool {

    private final AchievementThreadPoolProperties achievementProperties;

    @Bean()
    public Executor achievementHandlingExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(achievementProperties.getCorePoolSize());
        executor.setMaxPoolSize(achievementProperties.getMaxPoolSize());
        executor.setQueueCapacity(achievementProperties.getQueueCapacity());
        executor.setThreadNamePrefix(achievementProperties.getThreadNamePrefix());
        executor.initialize();
        return executor;
    }
}