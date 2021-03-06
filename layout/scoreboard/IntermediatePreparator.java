package scoreboard;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import data.Standings;

public abstract class IntermediatePreparator {

	public abstract void showSplitScreen(Scoreboard scoreboard,
			Standings standings, boolean tradVP);

	void createPointsArray(Scoreboard scoreboard) {
		Rectangle ptRect = new Rectangle();
		
		ptRect.setX (scoreboard.getPtUnderLayX ());
		ptRect.setY (scoreboard.getPtUnderLayY ());
		ptRect.setId ("ptHolder");
		ptRect.setWidth (scoreboard.getPtUnderLayWidth());
		ptRect.setHeight (scoreboard.getPtUnderLayHeight());
		ptRect.setFill (new ImagePattern (scoreboard.getDataCarrier().ptHolder));
		
		ArrayList<Rectangle> pointViews = new ArrayList<> ();
		int pointCounter = 0;
		
		for (Image image : scoreboard.getDataCarrier().getPointsTokens ()) {
			Rectangle pointView = new Rectangle();
			pointView.setWidth (scoreboard.getPointTokenWidth ());
			pointView.setHeight (scoreboard.getPointTokenHeight ());
			pointView.setFill (new ImagePattern (image));
			pointView.setId ("P" + indicesToPoints ((pointCounter + 1) % 10));

			pointView.setX (scoreboard.getPointTokenXOffset() + pointView.getWidth () * pointCounter++);
			pointView.setY (scoreboard.getPointTokenYOffset ());
			pointViews.add (pointView);
		}

		scoreboard.setPointViews (pointViews);

		scoreboard.getRoot().getChildren().add(ptRect);
		for (Rectangle rect : pointViews) {
			scoreboard.getRoot().getChildren ().add (rect);
		}
	}

	int indicesToPoints(int index) {
		if (index == 0) return 12;
		if (index == 9) return 10;
		
		return index;
	}
}
