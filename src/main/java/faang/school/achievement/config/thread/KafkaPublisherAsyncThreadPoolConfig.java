package faang.school.achievement.config.thread;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class KafkaPublisherAsyncThreadPoolConfig {
    @Value("${app.async.kafka.core-pool-size}")
    private int corePoolSize;

    @Value("${app.async.kafka.max-pool-size}")
    private int maxPoolSize;

    @Bean
    public Executor kafkaPublisherExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        return executor;
    }
}
