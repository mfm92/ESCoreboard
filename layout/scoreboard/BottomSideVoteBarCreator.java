package scoreboard;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
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

public class BottomSideVoteBarCreator extends VoteSideBarCreator {

	int nrOfCalled = 0;

	@Override
	public void makeSideOfScoreboard(Group group, Participant voter, Participant next,
			Scoreboard scoreboard) {

		nrOfCalled++;

		group.getChildren ().remove (group.lookup ("#sidebar"));
		Group sidebar = new Group ();
		sidebar.setId ("sidebar");

		// VOTER'S FLAG
		Rectangle voteFlagUnderlay = RectangleBuilder
				.create ()
				.width (0.2 * scoreboard.getRightBarWidth ())
				.height (scoreboard.getBottomBarHeight())
				.x (scoreboard.getRightBarX())
				.y (scoreboard.getBottomBarY())
				.fill (new ImagePattern (scoreboard.getDataCarrier().voteFlagUnderlay))
				.build ();

		ImageView voterFlag = ImageViewBuilder.create ()
				.image (voter.getFlag ())
				.fitWidth (voteFlagUnderlay.getWidth ())
				.fitHeight (voteFlagUnderlay.getHeight ())
				.layoutX (voteFlagUnderlay.getX ())
				.layoutY (voteFlagUnderlay.getY ()).build ();

		// VOTE NAME
		Rectangle currentVoterUnderlay = RectangleBuilder.create ()
				.width (0.65 * scoreboard.getRightBarWidth())
				.height (voteFlagUnderlay.getHeight ())
				.x (voteFlagUnderlay.getX () + 0.2 * scoreboard.getRightBarWidth())
				.y (voteFlagUnderlay.getY ())
				.fill (new ImagePattern (scoreboard.getDataCarrier().voteUnderlay))
				.build ();

		Text currentVoter = TextBuilder.create ()
				.text (voter.getName ()).fill (Color.WHITE)
				.font (Font.font (scoreboard.getDataCarrier().font_1, FontWeight.SEMI_BOLD, 45 * (scoreboard.getScreenWidth () / 1920d)))
				.build ();

		VBox currentVoterVBox = VBoxBuilder.create ()
				.layoutX (currentVoterUnderlay.getX ())
				.layoutY (voteFlagUnderlay.getY ())
				.prefHeight (voteFlagUnderlay.getHeight ())
				.prefWidth (currentVoterUnderlay.getWidth ())
				.children (currentVoter).alignment (Pos.CENTER).build ();

		// ADD COUNTER
		Rectangle counterUnderlay = RectangleBuilder
				.create ()
				.width (0.15 * scoreboard.getRightBarWidth())
				.height (voteFlagUnderlay.getHeight ())
				.x (scoreboard.getRightBarX() + 0.85 * scoreboard.getRightBarWidth())
				.y (voteFlagUnderlay.getY ())
				.fill (new ImagePattern (
						scoreboard.getDataCarrier().voteCounterULSmall)).build ();

		Text counter = TextBuilder
				.create ()
				.text ((int) (Math.ceil ((nrOfCalled + 1) / 2)) + " / "
						+ scoreboard.getDataCarrier().voteMap.size ())
				.fill (Color.WHITE)
				.font (Font.font (scoreboard.getDataCarrier().font_1, FontWeight.LIGHT, 24 * (scoreboard.getScreenWidth () / 1920d)))
				.build ();

		VBox counterVBox = VBoxBuilder.create ()
				.layoutX (counterUnderlay.getX ())
				.layoutY (counterUnderlay.getY ())
				.prefHeight (counterUnderlay.getHeight ())
				.prefWidth (counterUnderlay.getWidth ()).children (counter)
				.alignment (Pos.CENTER).build ();

		// ADD TO GROUPS
		sidebar.getChildren ().addAll (voteFlagUnderlay, voterFlag,
				currentVoterUnderlay, currentVoterVBox, counterUnderlay,
				counterVBox);

		group.getChildren ().add (sidebar);
	}
}
