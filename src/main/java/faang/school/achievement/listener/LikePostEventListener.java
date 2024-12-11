package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.LikePostEventDto;
import faang.school.achievement.handler.AllLoveAchievementHandler;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LikePostEventListener extends AbstractEventListener<LikePostEventDto>{

    @Value("${spring.data.redis.channel.like-post}")
    private String likePostNotification;

    public LikePostEventListener(AchievementService achievementService,
                                 ObjectMapper objectMapper,
                                 List<AllLoveAchievementHandler> handlers) {
        super(achievementService, objectMapper, handlers);
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {

    }

    @Override
    public MessageListenerAdapter getAdapter() {
        return new MessageListenerAdapter(this);
    }

    @Override
    public Topic getTopic() {
        return new ChannelTopic(likePostNotification);
    }


}
