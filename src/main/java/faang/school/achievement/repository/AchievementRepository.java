package faang.school.achievement.repository;

import faang.school.achievement.model.Achievement;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Long> {

    @Override
    @NonNull
    @CachePut(value = "ACHIEVEMENT", key = "#achievement.title")
    <S extends Achievement> S save(S achievement);

    @NonNull
    @Cacheable(value = "ACHIEVEMENT", key = "#title")
    Optional<Achievement> findByTitle(String title);
}
