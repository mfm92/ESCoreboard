package scoreboard;

import nations.Participant;
import javafx.scene.Group;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;

public abstract class AbstractTileUpdater extends TileUpdater {
	
	@Override
	public void updateTiles(Scoreboard scoreboard, Participant receiver) {
		int nrOfPart = scoreboard.participants.size ();
		prettyFormatting (scoreboard, nrOfPart, receiver);
	}

	@Override
	public void updateBackgroundOnly(Scoreboard scoreboard) {
		for (int position = 0; position < scoreboard.participants.size (); position++) {

			Group group = scoreboard.groupNationMap
					.get (scoreboard.participants.get (position));
			
			int sizeDenom = (int) Math.ceil ((double) scoreboard.participants.size () / (double) scoreboard.getColumnsNr());

			Rectangle base = RectangleBuilder
					.create ()
					.width (scoreboard.getColumnNameWidth())
					.height (scoreboard.getHeight() / sizeDenom)
					.layoutX (0)
					.layoutY (0)
					.id ("base")
					.fill (new ImagePattern (
							(position < scoreboard.getSpecialBorder() ? scoreboard.utilities.nationTileBackgroundPQ
									: scoreboard.utilities.nationTileBackground)))
					.build ();

			group.getChildren ().remove (group.getChildren ().get (0));
			group.getChildren ().add (0, base);
		}
	}

	public abstract void prettyFormatting(Scoreboard scoreboard, int nrOfPart, Participant receiver);
}
