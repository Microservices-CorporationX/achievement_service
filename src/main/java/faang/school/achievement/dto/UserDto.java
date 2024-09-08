package faang.school.achievement.dto;

import lombok.Data;

@Data
public class UserDto {

    private long id;
    private String username;
    private String email;
    private String phone;
    private Long telegramId;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
