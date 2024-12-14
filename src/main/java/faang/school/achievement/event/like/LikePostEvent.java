package faang.school.achievement.event.like;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LikePostEvent {

    @NotNull
    private Long authorPostId;

    @NotNull
    private Long likedUserId;

    @NotNull
    private Long postId;

    private LocalDateTime likeTime;

}
