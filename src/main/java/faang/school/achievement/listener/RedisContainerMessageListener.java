package faang.school.achievement.listener;

import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public interface RedisContainerMessageListener {

    Topic getTopic();

    default MessageListenerAdapter getAdapter() {
        return new MessageListenerAdapter(this);
    }

}
