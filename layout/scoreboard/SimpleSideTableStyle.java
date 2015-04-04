package scoreboard;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import nations.Participant;

public class SimpleSideTableStyle extends SideOverviewTableCreator {

	@Override
	public Group createSideTable(Scoreboard scoreboard, ArrayList<Participant> finalists) {
		Group superText = new Group ();
		superText.setId ("superText");
		
		double scaleWidth = scoreboard.getScreenWidth () / 1920d;
		double scaleHeight = scoreboard.getScreenHeight() / 1080d;
		
		int textHeight = (int)(100d * scaleHeight);
		Color bgColor = Color.web ("#000033");

		Color bgColorLight = Color.web ("#990000");
		Color bgColorRed = Color.web ("#800000");

		Color bgColorLighter = Color.web ("#00004A");

		for (int i = 0; i < 6; i++) {
			Group textGroup = new Group ();
			textGroup.setId ("Table " + i);
			
			Rectangle rectangle = new Rectangle();
			rectangle.setX (850 * scaleWidth);
			rectangle.setY (124 * scaleHeight + i * textHeight);
			rectangle.setFill (bgColor);
			rectangle.setWidth (500 * scaleWidth);
			rectangle.setHeight (60 * scaleHeight);

			ImageView flag = new ImageView (finalists.get (i).getFlag ());
			flag.setLayoutX (rectangle.getX () + 5 * scaleWidth);
			flag.setLayoutY (rectangle.getY () + 5);
			flag.setFitHeight (50 * scaleHeight);
			flag.setFitWidth (80 * scaleWidth);

			Rectangle pointsBase = new Rectangle();
			pointsBase.setX (1330 * scaleWidth);
			pointsBase.setY (124 * scaleHeight + i * textHeight);
			pointsBase.setWidth (120 * scaleWidth);
			pointsBase.setHeight (textHeight);
			pointsBase.setFill (i % 2 == 0 ? bgColorLight : bgColorRed);

			Text textScore = new Text (finalists.get (i).getScore ()
					+ "");
			textScore.setFont (Font.font ("Roboto Lt", 46 * scaleWidth));
			textScore.setFill (Color.WHITE);

			VBox ptsVBox = new VBox ();
			ptsVBox.setLayoutX (pointsBase.getX ());
			ptsVBox.setLayoutY (pointsBase.getY ());
			ptsVBox.setPrefHeight (pointsBase.getHeight ());
			ptsVBox.setPrefWidth (pointsBase.getWidth ());
			ptsVBox.setAlignment (Pos.CENTER);
			ptsVBox.getChildren ().add (textScore);

			Rectangle rectangleEntry = new Rectangle();
			
			rectangleEntry.setX (rectangle.getX ());
			rectangleEntry.setY (124 * scaleHeight + i * textHeight + 60 * scaleHeight);
			rectangleEntry.setWidth (480 * scaleWidth);
			rectangleEntry.setHeight (40 * scaleHeight);
			rectangleEntry.setFill (bgColorLighter);

			Text textEntry = new Text (finalists.get (i).getEntry ()
					.getArtist ()
					+ " - " + finalists.get (i).getEntry ().getTitle ());
			textEntry.setFont (Font.font ("Roboto Lt",
					FontWeight.BOLD, scaleWidth * (textEntry.getText ().length () > 40 ? 16 : 20)));
			textEntry.setFill (Color.WHITE);

			VBox entryVBox = new VBox ();
			entryVBox.setLayoutX (rectangleEntry.getX () + 10 * scaleWidth);
			entryVBox.setLayoutY (rectangleEntry.getY () + 5);
			entryVBox.setPrefHeight (rectangleEntry.getHeight () - 5);
			entryVBox.setPrefWidth (rectangleEntry.getWidth () - 20 * scaleWidth);
			entryVBox.setAlignment (Pos.CENTER_LEFT);
			entryVBox.getChildren ().add (textEntry);
			
			Text textNation = new Text (finalists.get (i).getName ());
			textNation.setFont (Font
					.font ("Roboto Lt", FontWeight.BOLD, scaleWidth * (textNation.getText ().length () > 14 ? 26 : 40)));
			textNation.setFill (Color.WHITE);

			VBox nationVBox = new VBox ();
			nationVBox.setLayoutX (rectangle.getX () + flag.getFitWidth () + 18 * scaleWidth);
			nationVBox.setLayoutY (rectangle.getY () + 5 * scaleHeight);
			nationVBox.setPrefHeight (rectangle.getHeight () - 10 * scaleHeight);
			nationVBox.setPrefWidth (rectangle.getWidth () - 20 * scaleWidth);
			nationVBox.setAlignment (Pos.CENTER_LEFT);
			nationVBox.getChildren ().add (textNation);

			textGroup.getChildren ().addAll (rectangle, pointsBase, flag,
					rectangleEntry, ptsVBox, entryVBox, nationVBox);
			superText.getChildren ().add (textGroup);
		}
		
		Rectangle r = new Rectangle();
		r.setX (850 * scaleWidth);
		r.setY (50 * scaleHeight);
		r.setId ("Top6Rect");
		r.setWidth (600 * scaleWidth);
		r.setHeight (74 * scaleHeight);
		r.setFill (Color.ALICEBLUE);
		
		superText.getChildren ().add (r);

		Text top6Text = new Text ("Top 6");
		top6Text.setFill (Color.DARKGRAY);
		top6Text.setFont (Font.font ("Coolvetica RG", FontWeight.BOLD, 50 * scaleWidth));

		VBox top6VBox = new VBox ();
		top6VBox.setLayoutX (900 * scaleWidth);
		top6VBox.setLayoutY (50 * scaleHeight);
		top6VBox.setPrefHeight (60 * scaleHeight);
		top6VBox.setPrefWidth (600 * scaleWidth);
		top6VBox.setAlignment (Pos.CENTER);
		top6VBox.getChildren ().add (top6Text);

		superText.getChildren ().add (top6VBox);
		superText.setLayoutX (400 * scaleWidth);
		superText.setStyle ("-fx-border-color: white;"
				+ "-fx-border-style: dashed;"
				+ "-fx-border-width: 1 1 1 1");
		
		return superText;
	}

}
