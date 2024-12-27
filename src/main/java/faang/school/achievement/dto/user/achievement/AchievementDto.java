package faang.school.achievement.dto.user.achievement;

import faang.school.achievement.model.Rarity;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class AchievementDto {
    private long id;
    private String title;
    private String description;
    private Rarity rarity;
    private List<Long> userAchievementsIds;
    private List<Long> progressesIds;
    private long points;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
