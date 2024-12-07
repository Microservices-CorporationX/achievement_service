package faang.school.achievement;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableRetry
@EnableFeignClients("faang.school.achievement.client")
@SpringBootApplication
@EnableAspectJAutoProxy
public class AchievementServiceApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AchievementServiceApp.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
