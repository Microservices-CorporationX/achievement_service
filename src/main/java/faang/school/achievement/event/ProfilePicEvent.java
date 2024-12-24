package faang.school.achievement.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProfilePicEvent {

    private Long userId;
    private String profilePicUrl;
}