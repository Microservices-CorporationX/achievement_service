package faang.school.achievement.handler;

import faang.school.achievement.event.ProfilePicEvent;

public interface EventHandler {
    void handleEvent(ProfilePicEvent event);
}