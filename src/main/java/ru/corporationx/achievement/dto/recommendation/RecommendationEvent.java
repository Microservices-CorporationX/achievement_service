package ru.corporationx.achievement.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationEvent {
    private Long id;
    private Long authorId;
    private Long receiverId;
    private LocalDateTime createdAt;
}
