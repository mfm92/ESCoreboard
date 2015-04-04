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
			Scoreboard scoreboard) {

		nrOfCalled++;

		group.getChildren ().remove (group.lookup ("#sidebar"));
		Group sidebar = new Group ();
		sidebar.setId ("sidebar");

		// VOTER'S FLAG
		Rectangle voteFlagUnderlay = RectangleBuilder
				.create ()
				.width (scoreboard.getRightBarWidth ())
				.height (0.75 * scoreboard.rightBarHeight)
				.x (scoreboard.getRightBarX ())
				.y (scoreboard.rightBarY)
				.fill (new ImagePattern (scoreboard.getDataCarrier().voteFlagUnderlay))
				.build ();

		int specialFlagWidth = (int) (0.96 * scoreboard.getRightBarWidth());
		int specialFlagHeight = (int) (0.96 * scoreboard.getRightBarWidth());

		int normalFlagWidth = (int) (0.955 * scoreboard.getRightBarWidth());
		int normalFlagHeight = (int) ((0.595/0.955) * normalFlagWidth);

		ImageView voterFlag = ImageViewBuilder
				.create ()
				.image (scoreboard.isUsingSpecialFlags() ? scoreboard.getDataCarrier().diamondMap
						.get (voter) : voter.getFlag ())
				.fitWidth (scoreboard.isUsingSpecialFlags() ? specialFlagWidth : normalFlagWidth)
				.fitHeight (scoreboard.isUsingSpecialFlags() ? specialFlagHeight : normalFlagHeight).build ();

		HBox diamondVBox = new HBox ();
		diamondVBox.setLayoutX (voteFlagUnderlay.getX ());
		diamondVBox.setLayoutY (voteFlagUnderlay.getY () - 1);
		diamondVBox.setPrefHeight (0.75 * scoreboard.rightBarHeight + 5.5);
		diamondVBox.setPrefWidth (voteFlagUnderlay.getWidth ());
		diamondVBox.setAlignment (Pos.CENTER);
		diamondVBox.getChildren ().add (voterFlag);

		// VOTE NAME
		Rectangle currentVoterUnderlay = RectangleBuilder
				.create ()
				.width (voteFlagUnderlay.getWidth ())
				.height (0.15 * scoreboard.rightBarHeight)
				.x (voteFlagUnderlay.getX ())
				.y (scoreboard.rightBarY + 0.75 * scoreboard.rightBarHeight)
				.fill (new ImagePattern (scoreboard.getDataCarrier().voteNameUnderlay))
				.build ();

		Text currentVoter = TextBuilder
				.create ()
				.text (voter.getName ())
				.fill (Color.WHITE)
				.font (Font.font ("RobotoLt", FontWeight.SEMI_BOLD, voter
						.getName ().length () < 14 ? 74 * (scoreboard.getScreenWidth () / 1920d) 
								: 52 * (scoreboard.getScreenWidth () / 1920d))).build ();

		VBox currentVoterVBox = VBoxBuilder.create ()
				.layoutX (voteFlagUnderlay.getX ())
				.layoutY (scoreboard.rightBarY + 0.75 * scoreboard.rightBarHeight)
				.prefHeight (0.15 * scoreboard.rightBarHeight)
				.prefWidth (voteFlagUnderlay.getWidth ()).children (currentVoter)
				.alignment (Pos.CENTER).build ();

		// ADD COUNTER
		Rectangle counterUnderlay = RectangleBuilder.create ()
				.width (voteFlagUnderlay.getWidth ()).height (0.1 * scoreboard.rightBarHeight)
				.x (voteFlagUnderlay.getX ()).y (scoreboard.rightBarY + 0.9 * scoreboard.rightBarHeight)
				.fill (new ImagePattern (scoreboard.getDataCarrier().voteCounterUL))
				.build ();

		Text counter = TextBuilder
				.create ()
				.text ((int) (Math.ceil ((nrOfCalled + 1) / 2)) + " out of "
						+ scoreboard.getDataCarrier().voteMap.size ())
				.fill (Color.WHITE)
				.font (Font.font ("Roboto Lt", FontWeight.LIGHT, 32 * (scoreboard.getScreenWidth () / 1920d)))
				.build ();

		VBox counterVBox = VBoxBuilder.create ().layoutX (voteFlagUnderlay.getX ())
				.layoutY (counterUnderlay.getY ())
				.prefHeight (counterUnderlay.getHeight ())
				.prefWidth (voteFlagUnderlay.getWidth ()).children (counter)
				.alignment (Pos.CENTER).build ();

		// ADD TO GROUPS
		sidebar.getChildren ().addAll (
				voteFlagUnderlay, diamondVBox, currentVoterUnderlay,
				currentVoterVBox, counterUnderlay, counterVBox);

		group.getChildren ().add (sidebar);

	}
}
