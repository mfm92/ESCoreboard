package scoreboard;

import javafx.scene.Group;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import nations.Participant;

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

			Rectangle base = new Rectangle();
			
			base.setWidth (scoreboard.getColumnNameWidth());
			base.setHeight (scoreboard.getHeight() / sizeDenom);
			base.setLayoutX (0);
			base.setLayoutY (0);
			base.setId ("base");
			base.setFill (new ImagePattern (
							(position < scoreboard.getSpecialBorder() ? scoreboard.utilities.nationTileBackgroundPQ
									: scoreboard.utilities.nationTileBackground)));

			group.getChildren ().remove (group.getChildren ().get (0));
			group.getChildren ().add (0, base);
		}
	}

	public abstract void prettyFormatting(Scoreboard scoreboard, int nrOfPart, Participant receiver);
}
