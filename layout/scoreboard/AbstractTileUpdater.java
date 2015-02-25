package scoreboard;

import javafx.scene.Group;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;

public abstract class AbstractTileUpdater extends TileUpdater {
	
	@Override
	public void updateTiles(Scoreboard scoreboard) {
		int nrOfPart = scoreboard.participants.size ();
		prettyFormatting (scoreboard, nrOfPart);
	}

	@Override
	public void updateBackgroundOnly(Scoreboard scoreboard) {
		for (int position = 0; position < scoreboard.participants.size (); position++) {

			Group group = scoreboard.groupNationMap
					.get (scoreboard.participants.get (position));
			
			int sizeDenom = (int) Math.ceil ((double) scoreboard.participants.size () / (double) scoreboard.columnsNr);

			Rectangle base = RectangleBuilder
					.create ()
					.width (scoreboard.columnNameWidth)
					.height (scoreboard.height / sizeDenom)
					.layoutX (0)
					.layoutY (0)
					.id ("base")
					.fill (new ImagePattern (
							(position < scoreboard.specialBorder ? scoreboard.utilities.nationTileBackgroundPQ
									: scoreboard.utilities.nationTileBackground)))
					.build ();

			group.getChildren ().remove (group.getChildren ().get (0));
			group.getChildren ().add (0, base);
		}
	}

	public abstract void prettyFormatting(Scoreboard scoreboard, int nrOfPart);
}
