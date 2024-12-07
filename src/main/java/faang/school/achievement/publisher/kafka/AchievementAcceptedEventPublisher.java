package faang.school.achievement.publisher.kafka;

import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.kafka.event.AchievementAcceptedEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class AchievementAcceptedEventPublisher {
    @Value("${kafka.topic.achievement-accepted-topic}")
    private String achievementAcceptedTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

//    @Async("kafkaPublisherExecutor")
    @AfterReturning(
            pointcut = "@annotation(faang.school.achievement.annotations.aop.AchievementAcceptedAspect)",
            returning = "returnValue"
    )
    public void publishPostEvent(Object returnValue) {
        UserAchievement userAchievement = (UserAchievement) returnValue;
        AchievementAcceptedEvent achievementAcceptedEvent = convert(userAchievement);
//        ProducerRecord<String, Object> pr = new ProducerRecord<>(achievementAcceptedTopic, achievementAcceptedEvent);
        try {
//            var result = kafkaTemplate.send(pr).get();
            var result = kafkaTemplate.send(achievementAcceptedTopic, achievementAcceptedEvent).get();
            System.out.println();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private AchievementAcceptedEvent convert(UserAchievement userAchievement) {
        return AchievementAcceptedEvent.builder()
                .userId(userAchievement.getUserId())
                .points(userAchievement.getAchievement().getPoints())
                .title(userAchievement.getAchievement().getTitle())
                .build();
    }
}
