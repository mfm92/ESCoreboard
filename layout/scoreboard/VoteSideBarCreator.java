package scoreboard;

import javafx.scene.Group;
import nations.Participant;

public abstract class VoteSideBarCreator {

	public abstract void makeSideOfScoreboard(Group group, Participant voter, Scoreboard scoreboard);

}
