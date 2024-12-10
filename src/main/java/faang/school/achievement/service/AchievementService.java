package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.mapper.achievement.AchievementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementCache achievementCache;
    private final AchievementMapper achievementMapper;

    public AchievementDto get(String title) {
        log.info("Requested achievement with title " + title);
        return achievementMapper.toDto(achievementCache.get(title));
    }

    public List<AchievementDto> getAll() {
        log.info("Requested all achievements");
        return achievementMapper.toDtoList(achievementCache.getAll());
    }
}
