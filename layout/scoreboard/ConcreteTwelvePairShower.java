package scoreboard;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import nations.Participant;

public class ConcreteTwelvePairShower extends TwelvePairShower {

	@Override
	public void addTwelvePair(Scoreboard scoreboard, Participant voter,
			Participant receiver) {
		scoreboard.getRoot().getChildren ().clear();
		
		double scaleWidth = scoreboard.getScreenWidth () / 1920d;
		double scaleHeight = scoreboard.getScreenHeight () / 1080d;
		
		Rectangle iR1 = new Rectangle();
		iR1.setWidth (scoreboard.getScreenWidth ());
		iR1.setHeight (scoreboard.getScreenHeight ());
		iR1.setId ("background");
		iR1.setId ("redBack");
		iR1.setFill (new ImagePattern (scoreboard.getDataCarrier().backgroundRed));

		scoreboard.getRoot().getChildren ().add (iR1);
		
		Rectangle iR2 = new Rectangle();
		iR2.setX (0);
		iR2.setY (780 * scaleHeight);
		iR2.setId ("12Strip");
		iR2.setWidth (scoreboard.getScreenWidth ());
		iR2.setHeight (270 * scaleHeight);
		iR2.setFill (new ImagePattern (scoreboard.getDataCarrier().ppraisbg));
		
		scoreboard.getRoot().getChildren ().add (iR2);
		
		Rectangle iR3 = new Rectangle();
		iR3.setWidth (216 * scaleWidth);
		iR3.setHeight (170 * scaleHeight);
		iR3.setX (100 * scaleWidth);
		iR3.setY (830 * scaleHeight);
		iR3.setId ("12D");
		iR3.setFill (new ImagePattern (scoreboard.getDataCarrier().pprais));
		
		scoreboard.getRoot().getChildren ().add (iR3);

		Text receiveNation = new Text();
		receiveNation.setX (500 * scaleWidth);
		receiveNation.setY (795 * scaleHeight);
		receiveNation.setText (receiver.getName ());
		receiveNation.setFont (Font.font ("Coolvetica RG", FontWeight.BOLD, 81 * scaleWidth));
		receiveNation.setFill (Color.WHITE);

		Text receiveText = new Text();
		receiveText.setX (500 * scaleWidth);
		receiveText.setY (850 * scaleHeight);
		receiveText.setText ("received 12 points from");
		receiveText.setFont (Font.font ("Walkway SemiBold", FontWeight.BOLD, 42 * scaleWidth));
		receiveText.setFill (Color.WHITE);

		Text giverNation = new Text();
		giverNation.setX (500 * scaleWidth);
		giverNation.setY (900 * scaleHeight);
		giverNation.setText (voter.getName ());
		giverNation.setFont (Font.font ("Coolvetica RG", FontWeight.BOLD, 81 * scaleWidth));
		giverNation.setFill (Color.WHITE);

		VBox vBox = new VBox ();
		vBox.setId ("12Text");
		vBox.setLayoutX (300 * scaleWidth);
		vBox.setLayoutY (780 * scaleHeight);
		vBox.setPrefHeight (270 * scaleHeight);
		vBox.setPrefWidth (1620 * scaleWidth);
		vBox.getChildren ().addAll (receiveNation, receiveText, giverNation);
		vBox.setAlignment (Pos.CENTER);

		ImageView recFlag = new ImageView();
		recFlag.setImage (receiver.getFlag ());
		recFlag.setX (400 * scaleWidth);
		recFlag.setFitHeight (120 * scaleHeight);
		recFlag.setFitWidth (230 * scaleWidth);
		
		ImageView votFlag = new ImageView();
		votFlag.setImage (voter.getFlag ());
		votFlag.setX (1500 * scaleWidth);
		votFlag.setFitHeight (120 * scaleHeight);
		votFlag.setFitWidth (230 * scaleWidth);

		HBox hBox = new HBox ();
		hBox.setId ("12Flags");
		hBox.setLayoutX (480 * scaleWidth);
		hBox.setLayoutY (780 * scaleHeight);
		hBox.setPrefHeight (270 * scaleHeight);
		hBox.setPrefWidth (1320 * scaleWidth);
		hBox.getChildren ().addAll (recFlag, vBox, votFlag);
		hBox.setAlignment (Pos.CENTER);
		scoreboard.getRoot().getChildren ().add (hBox);
	}

}
