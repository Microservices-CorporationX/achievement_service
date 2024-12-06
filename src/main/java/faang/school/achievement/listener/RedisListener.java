package faang.school.achievement.listener;

import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public interface RedisListener {

    MessageListenerAdapter getAdapter();

    Topic getTopic();
}
