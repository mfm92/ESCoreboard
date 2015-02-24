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

	protected int pointsToIndices(int pts) {
		if (pts <= 8)
			return pts;
		if (pts == 10)
			return 9;
		if (pts == 12)
			return 0;

		System.out.println ("pointsToIndices returns sth baaaad, pts was: "
				+ pts);

		return 103859; // absolutely pointless huehue
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
