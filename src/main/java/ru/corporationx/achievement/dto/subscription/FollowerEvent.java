package ru.corporationx.achievement.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowerEvent {
    private Long followerId;
    private Long followeeId;
}