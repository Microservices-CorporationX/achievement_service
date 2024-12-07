package faang.school.achievement.event.project;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEvent {

    @NotNull
    private Long authorId;

    @NotNull
    private Long projectId;
}
