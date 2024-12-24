package faang.school.achievement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.event.AchievementEventDto;
import faang.school.achievement.handler.ConglomerateAchievementHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
public class ConglomerateAchievementEventListener implements MessageListener {

    private final ConglomerateAchievementHandler conglomerateAchievementHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ConglomerateAchievementEventListener(ConglomerateAchievementHandler conglomerateAchievementHandler) {
        this.conglomerateAchievementHandler = conglomerateAchievementHandler;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received conglomerate achievement event");
        String jsonMessage = new String(message.getBody());
        AchievementEventDto achievementEventDto;

        try {
            achievementEventDto = objectMapper
                    .readValue(jsonMessage, AchievementEventDto.class);
            achievementEventDto.setAchievementTitle("CONGLOMERATE");
        } catch (JsonProcessingException e) {
            log.error("Message could not be mapped to dto", e);
            throw new IllegalStateException(e);
        }

        conglomerateAchievementHandler.handleEvent(achievementEventDto);
    }
}
