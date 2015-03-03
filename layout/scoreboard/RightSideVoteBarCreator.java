package scoreboard;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import nations.Participant;

public class RightSideVoteBarCreator extends VoteSideBarCreator {

	int nrOfCalled = 0;

	@Override
	public void makeSideOfScoreboard(Group group, Participant voter,
			Scoreboard scoreboard, int underlayHeight, int underlayY) {

		nrOfCalled++;

		group.getChildren ().remove (group.lookup ("#sidebar"));
		Group sidebar = new Group ();
		sidebar.setId ("sidebar");

		// VOTING CALL
		Rectangle voteUnderlay = RectangleBuilder.create ().width (scoreboard.getRightBarWidth())
				.height (0.2 * underlayHeight).x (scoreboard.getRightBarX()).y (underlayY)
				.fill (new ImagePattern (scoreboard.utilities.voteUnderlay))
				.build ();

		Text voteText = TextBuilder.create ().text ("voting")
				.fill (Color.WHITE)
				.font (Font.font ("Coolvetica RG", FontWeight.SEMI_BOLD, 44))
				.build ();

		VBox voteVBox = VBoxBuilder.create ()
				.prefWidth (voteUnderlay.getWidth ())
				.prefHeight (voteUnderlay.getHeight ())
				.layoutX (voteUnderlay.getX ()).layoutY (voteUnderlay.getY ())
				.children (voteText).alignment (Pos.CENTER).build ();

		// VOTER'S FLAG
		Rectangle voteFlagUnderlay = RectangleBuilder
				.create ()
				.width (voteUnderlay.getWidth ())
				.height (0.5 * underlayHeight)
				.x (voteUnderlay.getX ())
				.y (underlayY + 0.2 * underlayHeight)
				.fill (new ImagePattern (scoreboard.utilities.voteFlagUnderlay))
				.build ();

		int specialFlagWidth = (int) (0.8 * scoreboard.getRightBarWidth());
		int specialFlagHeight = (int) (0.8 * scoreboard.getRightBarWidth());

		int normalFlagWidth = (int) (0.8 * scoreboard.getRightBarWidth());
		int normalFlagHeight = (int) ((0.55/0.8) * normalFlagWidth);

		ImageView voterFlag = ImageViewBuilder
				.create ()
				.image (scoreboard.useSpecialFlags ? scoreboard.utilities.diamondMap
						.get (voter) : voter.getFlag ())
				.fitWidth (scoreboard.useSpecialFlags ? specialFlagWidth : normalFlagWidth)
				.fitHeight (scoreboard.useSpecialFlags ? specialFlagHeight : normalFlagHeight).build ();

		HBox diamondVBox = new HBox ();
		diamondVBox.setLayoutX (voteUnderlay.getX ());
		diamondVBox.setLayoutY (voteUnderlay.getY ()
				+ voteUnderlay.getHeight ());
		diamondVBox.setPrefHeight (0.5 * underlayHeight);
		diamondVBox.setPrefWidth (voteUnderlay.getWidth ());
		diamondVBox.setAlignment (Pos.CENTER);
		diamondVBox.getChildren ().add (voterFlag);

		// VOTE NAME
		Rectangle currentVoterUnderlay = RectangleBuilder
				.create ()
				.width (voteUnderlay.getWidth ())
				.height (0.2 * underlayHeight)
				.x (voteUnderlay.getX ())
				.y (underlayY + 0.7 * underlayHeight)
				.fill (new ImagePattern (scoreboard.utilities.voteNameUnderlay))
				.build ();

		Text currentVoter = TextBuilder
				.create ()
				.text (voter.getName ())
				.fill (Color.WHITE)
				.font (Font.font ("Coolvetica RG", FontWeight.SEMI_BOLD, voter
						.getName ().length () < 14 ? 82 : 58)).build ();

		VBox currentVoterVBox = VBoxBuilder.create ()
				.layoutX (voteUnderlay.getX ())
				.layoutY (underlayY + 0.7 * underlayHeight)
				.prefHeight (0.2 * underlayHeight)
				.prefWidth (voteUnderlay.getWidth ()).children (currentVoter)
				.alignment (Pos.CENTER).build ();

		// ADD COUNTER
		Rectangle counterUnderlay = RectangleBuilder.create ()
				.width (voteUnderlay.getWidth ()).height (0.1 * underlayHeight)
				.x (voteUnderlay.getX ()).y (underlayY + 0.9 * underlayHeight)
				.fill (new ImagePattern (scoreboard.utilities.voteCounterUL))
				.build ();

		Text counter = TextBuilder
				.create ()
				.text ((int) (Math.ceil ((nrOfCalled + 1) / 2)) + " out of "
						+ scoreboard.utilities.voteMap.size ())
				.fill (Color.WHITE)
				.font (Font.font ("Coolvetica RG", FontWeight.LIGHT, 32))
				.build ();

		VBox counterVBox = VBoxBuilder.create ().layoutX (voteUnderlay.getX ())
				.layoutY (counterUnderlay.getY ())
				.prefHeight (counterUnderlay.getHeight ())
				.prefWidth (voteUnderlay.getWidth ()).children (counter)
				.alignment (Pos.CENTER).build ();

		// ADD TO GROUPS
		sidebar.getChildren ().addAll (voteUnderlay, voteVBox,
				voteFlagUnderlay, diamondVBox, currentVoterUnderlay,
				currentVoterVBox, counterUnderlay, counterVBox);

		group.getChildren ().add (sidebar);

	}
}
