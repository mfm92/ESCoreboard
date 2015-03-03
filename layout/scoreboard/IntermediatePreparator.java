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
		
		ptRect.setX (scoreboard.getGlobalXOffset());
		ptRect.setY (scoreboard.getGlobalYOffset() + scoreboard.getHeight() + scoreboard.getBottomScoreboardOffset());
		ptRect.setId ("ptHolder");
		ptRect.setWidth (scoreboard.getPtUnderLayWidth());
		ptRect.setHeight (scoreboard.getPtUnderLayHeight());
		ptRect.setFill (new ImagePattern (scoreboard.getUtilities().ptHolder));
		
		ArrayList<Rectangle> pointViews = new ArrayList<> ();
		int pointCounter = 0;
		
		for (Image image : scoreboard.getUtilities().getPointsTokens ()) {
			Rectangle pointView = new Rectangle();
			pointView.setWidth (scoreboard.getFlagWidth());
			pointView.setHeight (scoreboard.getFlagHeight());
			pointView.setFill (new ImagePattern (image));
			pointView.setId ("P" + indicesToPoints ((pointCounter + 1) % 10));

			pointView.setX (15 + scoreboard.getPointTokenXOffset() + pointView.getWidth () * pointCounter++);
			pointView.setY (3 + scoreboard.getPointTokenYOffset());
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
