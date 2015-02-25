package scoreboard;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import nations.Participant;

public class SimpleSideTableStyle extends SideOverviewTableCreator {

	@Override
	public Group createSideTable(ArrayList<Participant> finalists) {
		Group superText = new Group ();
		superText.setId ("superText");
		int textHeight = 100;

		Color bgColor = Color.web ("#000033");

		Color bgColorLight = Color.web ("#990000");
		Color bgColorRed = Color.web ("#800000");

		Color bgColorLighter = Color.web ("#00004A");

		for (int i = 0; i < 6; i++) {
			Group textGroup = new Group ();
			textGroup.setId ("Table " + i);
			Rectangle rectangle = RectangleBuilder.create ().x (900)
					.y (124 + i * textHeight).fill (bgColor).width (500)
					.height (60).build ();

			ImageView flag = new ImageView (finalists.get (i).getFlag ());
			flag.setLayoutX (rectangle.getX () + 5);
			flag.setLayoutY (rectangle.getY () + 5);
			flag.setFitHeight (50);
			flag.setFitWidth (80);

			Rectangle pointsBase = RectangleBuilder.create ().x (1380)
					.y (124 + i * textHeight).width (120).height (textHeight)
					.fill (i % 2 == 0 ? bgColorLight : bgColorRed).build ();

			Text textScore = new Text (finalists.get (i).getScore ().get ()
					+ "");
			textScore.setFont (Font.font ("Inconsolata", textScore.getText ()
					.length () > 15 ? 26 : 38));
			textScore.setFill (Color.WHITE);

			VBox ptsVBox = new VBox ();
			ptsVBox.setLayoutX (pointsBase.getX ());
			ptsVBox.setLayoutY (pointsBase.getY ());
			ptsVBox.setPrefHeight (pointsBase.getHeight ());
			ptsVBox.setPrefWidth (pointsBase.getWidth ());
			ptsVBox.setAlignment (Pos.CENTER);
			ptsVBox.getChildren ().add (textScore);

			Rectangle rectangleEntry = RectangleBuilder.create ().x (900)
					.y (124 + i * textHeight + 60).width (480).height (40)
					.fill (bgColorLighter).build ();

			Text textEntry = new Text (finalists.get (i).getEntry ()
					.getArtist ()
					+ " - " + finalists.get (i).getEntry ().getTitle ());
			textEntry.setFont (Font.font ("Liberation Sans Standard",
					FontWeight.BOLD, 24));
			textEntry.setFill (Color.WHITE);

			VBox entryVBox = new VBox ();
			entryVBox.setLayoutX (rectangleEntry.getX () + 10);
			entryVBox.setLayoutY (rectangleEntry.getY () + 5);
			entryVBox.setPrefHeight (rectangleEntry.getHeight () - 5);
			entryVBox.setPrefWidth (rectangleEntry.getWidth () - 20);
			entryVBox.setAlignment (Pos.CENTER_LEFT);
			entryVBox.getChildren ().add (textEntry);

			Text textNation = new Text (finalists.get (i).getName ().get ());
			textNation.setFont (Font
					.font ("Coolvetica RG", FontWeight.BOLD, 46));
			textNation.setFill (Color.WHITE);

			VBox nationVBox = new VBox ();
			nationVBox.setLayoutX (rectangle.getX () + 110);
			nationVBox.setLayoutY (rectangle.getY () + 5);
			nationVBox.setPrefHeight (rectangle.getHeight () - 10);
			nationVBox.setPrefWidth (rectangle.getWidth () - 20);
			nationVBox.setAlignment (Pos.CENTER_LEFT);
			nationVBox.getChildren ().add (textNation);

			textGroup.getChildren ().addAll (rectangle, pointsBase, flag,
					rectangleEntry, ptsVBox, entryVBox, nationVBox);
			superText.getChildren ().add (textGroup);
		}

		superText.getChildren ().add (
				RectangleBuilder.create ().x (900).y (50).id ("Top6Rect")
						.width (600).height (74).fill (Color.ALICEBLUE)
						.build ());

		Text top6Text = new Text ("Top 6");
		top6Text.setFill (Color.DARKGRAY);
		top6Text.setFont (Font.font ("Coolvetica RG", FontWeight.BOLD, 50));

		VBox top6VBox = new VBox ();
		top6VBox.setLayoutX (900);
		top6VBox.setLayoutY (50);
		top6VBox.setPrefHeight (60);
		top6VBox.setPrefWidth (600);
		top6VBox.setAlignment (Pos.CENTER);
		top6VBox.getChildren ().add (top6Text);

		superText.getChildren ().add (top6VBox);
		superText.setLayoutX (400);

		return superText;
	}

}
