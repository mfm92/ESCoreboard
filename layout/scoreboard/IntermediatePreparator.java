package scoreboard;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import data.Standings;

public abstract class IntermediatePreparator {

	public abstract void showSplitScreen(Scoreboard scoreboard,
			Standings standings, boolean tradVP);

	void createPointsArray(Scoreboard scoreboard) {
		Rectangle ptRect = RectangleBuilder.create ()
			.x (scoreboard.getGlobalXOffset())
			.y (scoreboard.getGlobalYOffset() + scoreboard.getHeight() + scoreboard.getBottomScoreboardOffset())
			.id ("ptHolder")
			.width (scoreboard.getPtUnderLayWidth())
			.height (scoreboard.getPtUnderLayHeight())
			.fill (new ImagePattern (scoreboard.utilities.ptHolder))
			.build ();
		
		ArrayList<Rectangle> pointViews = new ArrayList<> ();
		int pointCounter = 0;
		for (Image image : scoreboard.utilities.getPointsTokens ()) {
			Rectangle pointView = RectangleBuilder.create ()
				.width (scoreboard.getFlagWidth())
				.height (scoreboard.getFlagHeight())
				.fill (new ImagePattern (image))
				.id ("P" + indicesToPoints ((pointCounter + 1) % 10))
				.build ();

			pointView.setX (15 + scoreboard.getPointTokenXOffset() + pointView.getWidth () * pointCounter++);
			pointView.setY (3 + scoreboard.getPointTokenYOffset());
			pointViews.add (pointView);
		}

		scoreboard.pointViews = pointViews;

		scoreboard.root.getChildren().add(ptRect);
		for (Rectangle rect : pointViews) {
			scoreboard.root.getChildren ().add (rect);
		}
	}

	int indicesToPoints(int index) {
		if (index == 0) return 12;
		if (index == 9) return 10;
		
		return index;
	}
}
