package faang.school.achievement.repository;

import faang.school.achievement.model.UserAchievement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAchievementRepository extends CrudRepository<UserAchievement, Long> {

    @Query(nativeQuery = true, value = """
                    INSERT INTO user_achievement (user_id, achievement_id)
                            VALUES (:userId, :achievementId)
                            ON CONFLICT (user_id, achievement_id) DO NOTHING;
            """)
    boolean giveAchievement(long userId, long achievementId);

    @Query(nativeQuery = true, value = """
                    SELECT CASE WHEN COUNT(ua) > 0 THEN true ELSE false END
                    FROM UserAchievement ua
                    WHERE ua.userId = :userId AND ua.achievement.id = :achievementId
            """)
    boolean existsByUserIdAndAchievementId(long userId, long achievementId);

    List<UserAchievement> findByUserId(long userId);
}
