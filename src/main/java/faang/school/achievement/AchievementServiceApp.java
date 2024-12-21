package faang.school.achievement;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.RedisConfigChannelsProperties;
import faang.school.achievement.config.RedisConfigProperties;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients("faang.school.achievement.client")
@EnableAsync
@EnableRetry
@EnableCaching
@ConfigurationPropertiesScan("faang.school.achievement.config")
@EnableConfigurationProperties({RedisConfigProperties.class, RedisConfigChannelsProperties.class})
public class AchievementServiceApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AchievementServiceApp.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
