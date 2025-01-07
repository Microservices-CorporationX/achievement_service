package faang.school.achievement.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MentorshipAcceptedEvent {
   private long  receiverId;
   private long requesterId;

    public long getAuthorForAchievements() {
        return receiverId;
    }
}
