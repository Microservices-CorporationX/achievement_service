package faang.school.achievement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import faang.school.achievement.model.Rarity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementDto {
    private long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotNull
    private Rarity rarity;

    @NotEmpty
    private long points;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
