package faang.school.achievement.config.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class RedisProperties {

    @Value("${spring.data.redis.channel.achievement}")
    private String achievementChannel;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.channel.profile-pic}")
    private String profilePicChannel;
    @Value("${spring.data.redis.channel.mentorship-channel}")
    private String mentorshipChannel;
}