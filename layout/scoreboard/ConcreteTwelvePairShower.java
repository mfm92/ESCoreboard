package scoreboard;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import nations.Participant;

public class ConcreteTwelvePairShower extends TwelvePairShower {

	@Override
	public void addTwelvePair(Scoreboard scoreboard, Participant voter,
			Participant receiver) {
		scoreboard.root.getChildren ().remove (scoreboard.background);

		scoreboard.root.getChildren ().add (
				RectangleBuilder
						.create ()
						.width (1920)
						.id ("background")
						.height (1080)
						.id ("redBack")
						.fill (new ImagePattern (
								scoreboard.utilities.backgroundRed)).build ());
		scoreboard.root
				.getChildren ()
				.add (RectangleBuilder
						.create ()
						.x (0)
						.y (780)
						.id ("12Strip")
						.width (1920)
						.height (270)
						.fill (new ImagePattern (scoreboard.utilities.ppraisbg))
						.build ());
		scoreboard.root.getChildren ().add (
				RectangleBuilder.create ().x (100).y (830).id ("12D")
						.width (216).height (170)
						.fill (new ImagePattern (scoreboard.utilities.pprais))
						.build ());

		Text receiveNation = TextBuilder.create ().x (500).y (795)
				.text (receiver.getName ())
				.font (Font.font ("Coolvetica RG", FontWeight.BOLD, 81))
				.fill (Color.WHITE).build ();

		Text receiveText = TextBuilder.create ().x (500).y (850)
				.text ("received 12 points from")
				.font (Font.font ("Walkway SemiBold", FontWeight.BOLD, 42))
				.fill (Color.WHITE).build ();

		Text giverNation = TextBuilder.create ().x (500).y (900)
				.text (voter.getName ())
				.font (Font.font ("Coolvetica RG", FontWeight.BOLD, 81))
				.fill (Color.WHITE).build ();

		VBox vBox = new VBox ();
		vBox.setId ("12Text");
		vBox.setLayoutX (300);
		vBox.setLayoutY (780);
		vBox.setPrefHeight (270);
		vBox.setPrefWidth (1620);
		vBox.getChildren ().addAll (receiveNation, receiveText, giverNation);
		vBox.setAlignment (Pos.CENTER);

		ImageView recFlag = ImageViewBuilder.create ()
				.image (receiver.getFlag ()).x (400).fitHeight (120)
				.fitWidth (230).build ();

		ImageView votFlag = ImageViewBuilder.create ().image (voter.getFlag ())
				.x (1500).fitHeight (120).fitWidth (230).build ();

		HBox hBox = new HBox ();
		hBox.setId ("12Flags");
		hBox.setLayoutX (480);
		hBox.setLayoutY (780);
		hBox.setPrefHeight (270);
		hBox.setPrefWidth (1320);
		hBox.getChildren ().addAll (recFlag, vBox, votFlag);
		hBox.setAlignment (Pos.CENTER);
		scoreboard.root.getChildren ().add (hBox);
	}

}
