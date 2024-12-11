package faang.school.achievement.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.SkillAcquiredEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SkillEventListener extends AbstractEventListener<SkillAcquiredEvent> {

    @Value("${spring.data.redis.channel.skill}")
    private String channelName;

    public SkillEventListener(
            ObjectMapper objectMapper,
            List<EventHandler<SkillAcquiredEvent>> abstractEventHandlers
    ) {
        super(objectMapper, abstractEventHandlers);
    }

    @Override
    public Topic getTopic() {
        return new ChannelTopic(channelName);
    }

    @Override
    public Class<SkillAcquiredEvent> getEventClass() {
        return SkillAcquiredEvent.class;
    }
}
