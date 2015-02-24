package scoreboard;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import data.Standings;

public abstract class OneToSevenScreenCreator {

	public abstract void showSplitScreen(Scoreboard scoreboard,
			Standings standings);

	void createPointsArray(Scoreboard scoreboard) {
		ArrayList<Rectangle> pointViews = new ArrayList<> ();
		int pointCounter = 0;
		for (Image image : scoreboard.utilities.getPointsTokens ()) {
			Rectangle pointView = RectangleBuilder.create ().width (0.15*500)
					.height (0.7 * (((840) / ((scoreboard.participants.size () + 1) / scoreboard.columnsNr)))).fill 
						(new ImagePattern (image))
					.id ("P" + indicesToPoints ((pointCounter + 1) % 10))
					.build ();

			pointView.setX (100 + 73 * pointCounter++);
			pointView.setY (1000);
			pointViews.add (pointView);
		}

		scoreboard.pointViews = pointViews;

		for (Rectangle rect : pointViews) {
			scoreboard.root.getChildren ().add (rect);
		}
	}

	int indicesToPoints(int index) {
		if (index == 0)
			return 12;
		if (index >= 1 && index <= 8)
			return index;
		if (index == 9)
			return 10;

		System.out.println ("IndicesToPoints returns sth baaaad, index was: "
				+ index);

		return 103859; // absolutely pointless huehue
	}
}
