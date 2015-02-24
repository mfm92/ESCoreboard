package scoreboard;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import nations.Participant;

public class ConcreteVoteSideBarCreator extends VoteSideBarCreator {

	@Override
	public void makeSideOfScoreboard(Group group, Participant voter,
			Scoreboard scoreboard) {
		group.getChildren ().remove (group.lookup ("#sidebar"));
		Group sidebar = new Group ();
		sidebar.setId ("sidebar");

		int sizeDenom = ((scoreboard.participants.size () + 1) / scoreboard.columnsNr) - 1;

		double topBar = scoreboard.groupNationMap.get (
				scoreboard.participants.get (0)).getLayoutY ();
		double bottomBar = scoreboard.groupNationMap.get (
				scoreboard.participants.get (sizeDenom)).getLayoutY () + 60;

		Rectangle underlay = RectangleBuilder.create ().width (520)
				.height (bottomBar - topBar).x (1350).y (topBar)
				.fill (new Color (0.5, 0.5, 0.5, 0.5)).build ();

		Text currentVoter = TextBuilder
				.create ()
				.text ("voting now: ")
				.fill (Color.WHITE)
				.font (Font.font ("Coolvetica RG", FontWeight.SEMI_BOLD, voter
						.getName ().get ().length () < 14 ? 82 : 58)).x (underlay.getX () + 100)
				.y (underlay.getY ()).build ();

		VBox currentVoterVBox = VBoxBuilder.create ().layoutX (underlay.getX ())
				.layoutY (topBar + 20).prefHeight (100).prefWidth (520)
				.alignment (Pos.CENTER).build ();

		currentVoterVBox.getChildren ().add (currentVoter);

		Text currentVoter2 = TextBuilder
				.create ()
				.text (voter.getName ().get ())
				.fill (Color.WHITE)
				.font (Font.font ("Coolvetica RG", FontWeight.SEMI_BOLD, voter
						.getName ().get ().length () < 14 ? 82 : 58)).x (underlay.getX () + 110)
				.y (underlay.getY() + 700).build ();

		VBox currentVoter2VBox = VBoxBuilder.create ().layoutX (underlay.getX())
				.layoutY (underlay.getY() + 670).prefHeight (200).prefWidth (520)
				.alignment (Pos.CENTER).build ();

		currentVoter2VBox.getChildren ().add (currentVoter2);

		sidebar.getChildren ().addAll (underlay, currentVoterVBox,
				currentVoter2VBox);

		ImageView voterFlag = ImageViewBuilder.create ()
				.image (scoreboard.utilities.diamondMap.get (voter))
				.layoutX (underlay.getX() + 20).layoutY (underlay.getY() + 160).fitWidth (500).fitHeight (550)
				.build ();

		HBox diamondVBox = new HBox ();
		diamondVBox.setLayoutX (underlay.getX());
		diamondVBox.setLayoutY (underlay.getY() + 160);
		diamondVBox.setPrefHeight (550);
		diamondVBox.setPrefWidth (520);
		diamondVBox.setAlignment (Pos.CENTER);
		diamondVBox.getChildren ().add (voterFlag);

		sidebar.getChildren ().add (diamondVBox);
		group.getChildren ().add (sidebar);
	}
}
