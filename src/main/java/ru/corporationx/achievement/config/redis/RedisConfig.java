package ru.corporationx.achievement.config.redis;

import ru.corporationx.achievement.listener.comment.CommentEventListener;
import ru.corporationx.achievement.listener.mentorship.MentorshipEventListener;
import ru.corporationx.achievement.listener.recommendation.RecommendationEventListener;
import ru.corporationx.achievement.listener.subscription.FollowerEventListener;
import ru.corporationx.achievement.listener.team.TeamEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.team}")
    private String teamChannel;

    @Value("${spring.data.redis.channel.mentorship}")
    private String mentorshipChannel;

    @Value("${spring.data.redis.channel.recommendation}")
    private String recommendationChannel;

    @Value("${spring.data.redis.channel.comment}")
    private String commentChannel;

    @Value("${spring.data.redis.channel.project}")
    private String projectChannel;

    @Value("${spring.data.redis.channel.follower}")
    private String followerChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public MessageListenerAdapter teamListener(TeamEventListener teamEventListener) {
        return new MessageListenerAdapter(teamEventListener);
    }

    @Bean
    public ChannelTopic teamTopic() {
        return new ChannelTopic(teamChannel);
    }

    @Bean
    public MessageListenerAdapter mentorshipListener(MentorshipEventListener mentorshipEventListener) {
        return new MessageListenerAdapter(mentorshipEventListener);
    }

    @Bean
    public ChannelTopic mentorshipTopic() {
        return new ChannelTopic(mentorshipChannel);
    }

    @Bean
    public MessageListenerAdapter commentListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(commentChannel);
    }

    @Bean
    public MessageListenerAdapter recommendationListener(RecommendationEventListener recommendationEventListener) {
        return new MessageListenerAdapter(recommendationEventListener);
    }

    @Bean
    public ChannelTopic recommendationTopic() {
        return new ChannelTopic(recommendationChannel);
    }

    @Bean
    public ChannelTopic projectTopic() {
        return new ChannelTopic(projectChannel);
    }

    @Bean
    public MessageListenerAdapter followerListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public ChannelTopic followerTopic() {
        return new ChannelTopic(followerChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter teamListener,
                                                        MessageListenerAdapter mentorshipListener,
                                                        MessageListenerAdapter recommendationListener,
                                                        MessageListenerAdapter commentListener,
                                                        MessageListener projectEventListener,
                                                        MessageListenerAdapter followerListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(commentListener, commentTopic());
        container.addMessageListener(teamListener, teamTopic());
        container.addMessageListener(mentorshipListener, mentorshipTopic());
        container.addMessageListener(recommendationListener, recommendationTopic());
        container.addMessageListener(projectEventListener, projectTopic());
        container.addMessageListener(followerListener, followerTopic());
        return container;
    }
}
