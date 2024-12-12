package faang.school.achievement.handler.team;

import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.model.event.team.TeamEvent;
import org.springframework.stereotype.Component;

@Component
public abstract class TeamEventHandler implements EventHandler<TeamEvent> {

}
