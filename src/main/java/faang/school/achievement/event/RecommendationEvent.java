package faang.school.achievement.event;

public record RecommendationEvent(long authorId, long receiverId, long recommendationId) {
}
