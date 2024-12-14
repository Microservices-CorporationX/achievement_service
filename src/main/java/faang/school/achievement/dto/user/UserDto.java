package faang.school.achievement.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private long id;
    private String username;
    private boolean active;
    private String aboutMe;
    private String country;
    private Integer experience;
    private LocalDateTime createdAt;
    private List<Long> followersIds;
    private List<Long> followeesIds;
    private List<Long> menteesIds;
    private List<Long> mentorsIds;
    private List<Long> goalsIds;
    private List<Long> skillsIds;
    private Locale locale;
}
