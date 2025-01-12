package faang.school.achievement.config.redis;

import faang.school.achievement.message.consumer.MentorshipEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class MessageListenerConfig {

    private final MentorshipEventListener mentorshipEventListener;

    @Value("${spring.data.redis.channel.mentorship}")
    private String mentorshipEventTopicName;


    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(redisConnectionFactory);

        ChannelTopic mentorshipEventTopic = new ChannelTopic(mentorshipEventTopicName);
        MessageListenerAdapter mentorshipListenerAdapter = new MessageListenerAdapter(mentorshipEventListener);
        listenerContainer.addMessageListener(mentorshipListenerAdapter, mentorshipEventTopic);

        return listenerContainer;
    }
}
