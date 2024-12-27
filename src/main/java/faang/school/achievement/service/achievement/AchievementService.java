package faang.school.achievement.service.achievement;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.user.achievement.AchievementDto;
import faang.school.achievement.mapper.achievement.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.validator.achievement.AchievementServiceValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementServiceValidator validator;
    private final AchievementMapper achievementMapper;
    private final RedisTemplate<String, AchievementDto> redisTemplateAchievementCache;
    private static final String CACHE_KEY_PREFIX = "achievement:";

    public boolean hasAchievement(long userId, long achievementId) {
        log.info("validate Argument");
        validator.checkId(userId, achievementId);

        log.info("check whether the user has an achievement");
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    public Achievement getAchievementByTitle(String title) {
        log.info("validate Argument");
        validator.checkTitle(title);

        log.info("getting achievement from db by id");
        return achievementMapper.toEntity(getAchievementDtoByTitle(title));
    }

    public void createProgressIfNecessary(long userId, long achievementId) {
        log.info("validate Argument");
        validator.checkId(userId, achievementId);

        log.info("create new achievementProgress with given userId and achievementId");
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        log.info("validate Argument");
        validator.checkId(userId, achievementId);

        log.info("create new achievementProgress with given userId and achievementId if needed");
        createProgressIfNecessary(userId, achievementId);

        log.info("getting achievementProgress from db by userId and achievementId");
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Entity was not found at db"));
    }

    public void createNewUserAchievement(UserAchievement userAchievement) {
        log.info("validate Argument");
        validator.checkUserAchievement(userAchievement);

        log.info("add new userAchievement in db");
        userAchievementRepository.save(userAchievement);
    }
    // не работает ((((((( @Cacheable(value = "achievement",key = "#title")
    private AchievementDto getAchievementDtoByTitle(String title){
        AchievementDto cachedAchievementDto = (AchievementDto) redisTemplateAchievementCache.opsForValue().get(CACHE_KEY_PREFIX + title);
        if (cachedAchievementDto != null) {
            return cachedAchievementDto;
        }

        Achievement achievement = achievementRepository.getAchievementByTitle(title);
        AchievementDto achievementDto = achievementMapper.toDto(achievement);

        redisTemplateAchievementCache.opsForValue().set(CACHE_KEY_PREFIX + title, achievementDto);

        return achievementDto;
    }
}
