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
			Scoreboard scoreboard, int underlayHeight, int underlayY) {
		
		if (scoreboard.columnsNr < 3) {
			group.getChildren ().remove (group.lookup ("#sidebar"));
			Group sidebar = new Group ();
			sidebar.setId ("sidebar");

			Rectangle underlay = RectangleBuilder.create ().width (520)
					.height (underlayHeight).x (1350).y (underlayY)
					.fill (new Color (0.5, 0.5, 0.5, 0.5)).build ();

			Text currentVoter = TextBuilder
					.create ()
					.text ("voting now: ")
					.fill (Color.WHITE)
					.font (Font.font ("Coolvetica RG", FontWeight.SEMI_BOLD, voter
							.getName ().get ().length () < 14 ? 82 : 58)).x (underlay.getX () + 100)
					.y (underlay.getY ()).build ();

			VBox currentVoterVBox = VBoxBuilder.create ().layoutX (underlay.getX ())
					.layoutY (underlayY + 20).prefHeight (100).prefWidth (520)
					.alignment (Pos.CENTER).build ();

			currentVoterVBox.getChildren ().add (currentVoter);

			Text currentVoter2 = TextBuilder
					.create ()
					.text (voter.getName ().get ())
					.fill (Color.WHITE)
					.font (Font.font ("Coolvetica RG", FontWeight.SEMI_BOLD, voter
							.getName ().get ().length () < 14 ? 82 : 58)).x (underlay.getX () + 110)
					.y (underlay.getY() + 0.75 * underlayHeight).build ();

			VBox currentVoter2VBox = VBoxBuilder.create ().layoutX (underlay.getX())
					.layoutY (underlay.getY() + 0.7 * underlayHeight).prefHeight (0.25 * underlayHeight).prefWidth (520)
					.alignment (Pos.CENTER).build ();

			currentVoter2VBox.getChildren ().add (currentVoter2);

			sidebar.getChildren ().addAll (underlay, currentVoterVBox,
					currentVoter2VBox);

			int specialFlagWidth = (int) (0.45 * underlayHeight);
			int specialFlagHeight = (int) (0.55 * underlayHeight);
			int specialOffset = (int) (0.2 * underlayHeight);
			
			int normalFlagWidth = (int) (0.45 * underlayHeight);
			int normalFlagHeight = (int) (0.35 * underlayHeight);
			int normalOffset = (int) (0.3 * underlayHeight);
			
			ImageView voterFlag = ImageViewBuilder.create ()
					.image (scoreboard.useSpecialFlags ? scoreboard.utilities.diamondMap.get (voter) : voter.getFlag ())
					.layoutX (underlay.getX() + 20).layoutY (underlay.getY() + (scoreboard.useSpecialFlags ?
							specialOffset : normalOffset))
					.fitWidth (scoreboard.useSpecialFlags ? specialFlagWidth : normalFlagWidth)
					.fitHeight (scoreboard.useSpecialFlags ? specialFlagHeight : normalFlagHeight)
					.build ();

			HBox diamondVBox = new HBox ();
			diamondVBox.setLayoutX (underlay.getX());
			diamondVBox.setLayoutY (underlay.getY() + (scoreboard.useSpecialFlags ? specialOffset : normalOffset));
			diamondVBox.setPrefHeight (scoreboard.useSpecialFlags ? specialFlagHeight : normalFlagHeight);
			diamondVBox.setPrefWidth (520);
			diamondVBox.setAlignment (Pos.CENTER);
			diamondVBox.getChildren ().add (voterFlag);

			sidebar.getChildren ().add (diamondVBox);
			group.getChildren ().add (sidebar);	
			
		} else {
			//TODO: Create a vote bar in the bottom instead.
		}
	}
}
