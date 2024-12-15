package faang.school.achievement.repository;

import faang.school.achievement.model.Achievement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Long> {

    @Query("SELECT ac FROM Achievement ac WHERE ac.title = :title")
    Achievement getAchievementByTitle(@Param("title") String title);
}
