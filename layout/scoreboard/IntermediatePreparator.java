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
		ArrayList<Rectangle> pointViews = new ArrayList<> ();
		int pointCounter = 0;
		for (Image image : scoreboard.utilities.getPointsTokens ()) {
			Rectangle pointView = RectangleBuilder.create ()
					.width (scoreboard.flagWidth)
					.height (scoreboard.flagHeight)
					.fill (new ImagePattern (image))
					.id ("P" + indicesToPoints ((pointCounter + 1) % 10))
					.build ();

			pointView.setX (scoreboard.pointTokenXOffset + pointView.getWidth () * pointCounter++);
			pointView.setY (scoreboard.pointTokenYOffset);
			pointViews.add (pointView);
		}

		scoreboard.pointViews = pointViews;

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
