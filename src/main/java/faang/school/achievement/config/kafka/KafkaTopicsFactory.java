package faang.school.achievement.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsFactory {
    @Value("${kafka.partitions}")
    private int partitions;

    @Value("${kafka.replicas}")
    private int replicas;

    @Value("${kafka.topic.achievement-accepted-topic}")
    private String achievementAcceptedTopic;

    @Bean
    public NewTopic postPublishedTopic() {
        return TopicBuilder.name(achievementAcceptedTopic)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }
}
