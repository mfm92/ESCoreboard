package scoreboard;

import nations.Participant;

public abstract class TileUpdater {

	public abstract void updateTiles(Scoreboard scoreboard, Participant receiver);
	public abstract void updateBackgroundOnly(Scoreboard scoreboard);

}
