package scoreboard;

import java.util.ArrayList;

import javafx.scene.Group;
import nations.Participant;

public abstract class SideOverviewTableCreator {

	public abstract Group createSideTable(Scoreboard scoreboard, ArrayList<Participant> finalists);

}
