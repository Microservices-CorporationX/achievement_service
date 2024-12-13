package faang.school.achievement.listener;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public interface RedisContainerMessageListener {

    ChannelTopic getChannelTopic();

    default MessageListenerAdapter getAdapter(){
        return new MessageListenerAdapter(this);
    }
}
