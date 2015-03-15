package scoreboard;

import javafx.scene.Group;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import nations.Participant;

public abstract class AbstractTileUpdater extends TileUpdater {
	
	@Override
	public void updateTiles(Scoreboard scoreboard, Participant receiver) {
		int nrOfPart = scoreboard.getParticipants().size ();
		prettyFormatting (scoreboard, nrOfPart, receiver);
	}

	@Override
	public void updateBackgroundOnly(Scoreboard scoreboard) {
		
		Rectangle backgroundIntermediate = new Rectangle (scoreboard.getBackgroundWidth (), scoreboard.getBackgroundHeight ());
		backgroundIntermediate.setLayoutX ((scoreboard.getScreenWidth () - scoreboard.getBackgroundWidth ()) / 2);
		backgroundIntermediate.setLayoutY ((scoreboard.getScreenHeight () - scoreboard.getBackgroundHeight ()) / 2);
		backgroundIntermediate.setFill (new ImagePattern (scoreboard.getDataCarrier ().intermediateBackground));
		backgroundIntermediate.setId ("backgroundI");
		Rectangle background = (Rectangle)(scoreboard.getRoot ().getChildren ().get (0));
		
		Group newG = new Group (background, backgroundIntermediate);
		scoreboard.getRoot ().getChildren ().remove (background);
		scoreboard.getRoot().getChildren ().remove (scoreboard.getRoot ().lookup ("#backgroundI"));
		scoreboard.getRoot().getChildren ().add(0, newG);
		
		prettyFormatting (scoreboard, scoreboard.getParticipants ().size (), null);
	}

	public abstract void prettyFormatting(Scoreboard scoreboard, int nrOfPart, Participant receiver);
}
