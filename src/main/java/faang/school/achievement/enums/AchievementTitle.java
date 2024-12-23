package faang.school.achievement.enums;

import lombok.Getter;

@Getter
public enum AchievementTitle {
    COLLECTOR("COLLECTOR"),
    MR_PRODUCTIVITY("MR PRODUCTIVITY"),
    EXPERT("EXPERT"),
    SENSEI("SENSEI"),
    MANAGER("MANAGER"),
    CELEBRITY("CELEBRITY"),
    WRITER("WRITER"),
    HANDSOME("HANDSOME"),
    NICE_GUY("NICE GUY");

    private final String value;

    AchievementTitle(String value) {
        this.value = value;
    }
}
