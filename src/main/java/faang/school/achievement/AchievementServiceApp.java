package faang.school.achievement;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableFeignClients("faang.school.achievement.client")
@EnableRetry
@EnableCaching
@ConfigurationPropertiesScan("faang.school.achievement.config")
public class AchievementServiceApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AchievementServiceApp.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
