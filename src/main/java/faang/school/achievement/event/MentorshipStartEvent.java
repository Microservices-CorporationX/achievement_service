package faang.school.achievement.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipStartEvent {
    @NotNull
    @Min(0)
    private Long menteeId;
    @NotNull
    @Min(0)
    private Long mentorId;
}
