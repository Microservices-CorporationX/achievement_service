package faang.school.achievement.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MentorshipAcceptedEvent {
    private long requesterUserId;
    private long receiverUserId;
    private long userContextId;
}
