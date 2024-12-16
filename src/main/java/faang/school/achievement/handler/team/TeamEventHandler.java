package faang.school.achievement.handler.team;

import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.dto.team.TeamEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public abstract class TeamEventHandler implements EventHandler<TeamEvent> {

    @Async("teamEventPool")
    public abstract void handleEvent(TeamEvent event);
}
