package scoreboard;

import java.util.ArrayList;

import javafx.scene.Group;
import nations.Participant;

public abstract class SideOverviewTableCreator {

	public abstract Group createSideTable(ArrayList<Participant> finalists);

}
