package faang.school.achievement.dto.post;

import lombok.Builder;

@Builder
public record PostEvent(long userId, long postId) {
}
