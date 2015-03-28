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
		scoreboard.getRoot().getChildren ().remove (scoreboard.getBackground());
		
		Rectangle iR1 = new Rectangle();
		iR1.setWidth (scoreboard.getScreenWidth ());
		iR1.setHeight (scoreboard.getScreenHeight ());
		iR1.setId ("background");
		iR1.setId ("redBack");
		iR1.setFill (new ImagePattern (scoreboard.getDataCarrier().backgroundRed));

		scoreboard.getRoot().getChildren ().add (iR1);
		
		Rectangle iR2 = new Rectangle();
		iR2.setX (0);
		iR2.setY (780);
		iR2.setId ("12Strip");
		iR2.setWidth (scoreboard.getScreenWidth ());
		iR2.setHeight (270);
		iR2.setFill (new ImagePattern (scoreboard.getDataCarrier().ppraisbg));
		
		scoreboard.getRoot().getChildren ().add (iR2);
		
		Rectangle iR3 = new Rectangle();
		iR3.setWidth (216);
		iR3.setHeight (170);
		iR3.setX (100);
		iR3.setY (830);
		iR3.setId ("12D");
		iR3.setFill (new ImagePattern (scoreboard.getDataCarrier().pprais));
		
		scoreboard.getRoot().getChildren ().add (iR3);

		Text receiveNation = new Text();
		receiveNation.setX (500);
		receiveNation.setY (795);
		receiveNation.setText (receiver.getName ());
		receiveNation.setFont (Font.font ("Coolvetica RG", FontWeight.BOLD, 81));
		receiveNation.setFill (Color.WHITE);

		Text receiveText = new Text();
		receiveText.setX (500);
		receiveText.setY (850);
		receiveText.setText ("received 12 points from");
		receiveText.setFont (Font.font ("Walkway SemiBold", FontWeight.BOLD, 42));
		receiveText.setFill (Color.WHITE);

		Text giverNation = new Text();
		giverNation.setX (500);
		giverNation.setY (900);
		giverNation.setText (voter.getName ());
		giverNation.setFont (Font.font ("Coolvetica RG", FontWeight.BOLD, 81));
		giverNation.setFill (Color.WHITE);

		VBox vBox = new VBox ();
		vBox.setId ("12Text");
		vBox.setLayoutX (300);
		vBox.setLayoutY (780);
		vBox.setPrefHeight (270);
		vBox.setPrefWidth (1620);
		vBox.getChildren ().addAll (receiveNation, receiveText, giverNation);
		vBox.setAlignment (Pos.CENTER);

		ImageView recFlag = new ImageView();
		recFlag.setImage (receiver.getFlag ());
		recFlag.setX (400);
		recFlag.setFitHeight (120);
		recFlag.setFitWidth (230);
		
		ImageView votFlag = new ImageView();
		votFlag.setImage (voter.getFlag ());
		votFlag.setX (1500);
		votFlag.setFitHeight (120);
		votFlag.setFitWidth (230);

		HBox hBox = new HBox ();
		hBox.setId ("12Flags");
		hBox.setLayoutX (480);
		hBox.setLayoutY (780);
		hBox.setPrefHeight (270);
		hBox.setPrefWidth (1320);
		hBox.getChildren ().addAll (recFlag, vBox, votFlag);
		hBox.setAlignment (Pos.CENTER);
		scoreboard.getRoot().getChildren ().add (hBox);
	}

}
