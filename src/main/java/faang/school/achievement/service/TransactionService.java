package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementCacheDto;
import faang.school.achievement.enums.AchievementTitle;
import faang.school.achievement.event.RecommendationEvent;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AchievementService achievementService;
    private final AchievementProgressService achievementProgressService;
    private final UserAchievementService userAchievementService;
    private final AchievementMapper achievementMapper;

    @Transactional
    public void handle(RecommendationEvent event) {
        AchievementCacheDto achievementCacheDto = achievementService.getAchievementByTitle(AchievementTitle.NICE_GUY.name());
        if (!userAchievementService.hasAchievement(event.receiverId(), achievementCacheDto.getId())) {
            achievementProgressService.createProgressIfNecessary(event.receiverId(), achievementCacheDto.getId());
            AchievementProgress progress = achievementProgressService.getProgress(event.receiverId(), achievementCacheDto.getId());
            if (progress.getCurrentPoints() < achievementCacheDto.getPoints()) {
                achievementProgressService.incrementUserAchievementProgress(progress.getId());
            } else if (progress.getCurrentPoints() == achievementCacheDto.getPoints()) {
                Achievement achievement = achievementMapper.toEntity(achievementCacheDto);
                userAchievementService.giveAchievement(event.receiverId(), achievement);
            }
        }
    }
}
