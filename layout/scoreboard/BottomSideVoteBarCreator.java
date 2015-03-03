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
	public void makeSideOfScoreboard(Group group, Participant voter,
			Scoreboard scoreboard, int underlayWidth, int underlayX) {

		nrOfCalled++;

		group.getChildren ().remove (group.lookup ("#sidebar"));
		Group sidebar = new Group ();
		sidebar.setId ("sidebar");

		// VOTER'S FLAG
		Rectangle voteFlagUnderlay = RectangleBuilder
				.create ()
				.width (0.2 * underlayWidth)
				.height (scoreboard.getBottomBarHeight())
				.x (underlayX)
				.y (scoreboard.getBottomBarY())
				.fill (new ImagePattern (scoreboard.utilities.voteFlagUnderlay))
				.build ();

		ImageView voterFlag = ImageViewBuilder.create ()
				.image (voter.getFlag ())
				.fitWidth (voteFlagUnderlay.getWidth ())
				.fitHeight (voteFlagUnderlay.getHeight ())
				.layoutX (voteFlagUnderlay.getX ())
				.layoutY (voteFlagUnderlay.getY ()).build ();

		// VOTE NAME
		Rectangle currentVoterUnderlay = RectangleBuilder.create ()
				.width (0.65 * underlayWidth)
				.height (voteFlagUnderlay.getHeight ())
				.x (voteFlagUnderlay.getX () + 0.2 * underlayWidth)
				.y (voteFlagUnderlay.getY ())
				.fill (new ImagePattern (scoreboard.utilities.voteUnderlay))
				.build ();

		Text currentVoter = TextBuilder.create ()
				.text (voter.getName ()).fill (Color.WHITE)
				.font (Font.font ("Coolvetica RG", FontWeight.SEMI_BOLD, 45))
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
				.width (0.15 * underlayWidth)
				.height (voteFlagUnderlay.getHeight ())
				.x (underlayX + 0.85 * underlayWidth)
				.y (voteFlagUnderlay.getY ())
				.fill (new ImagePattern (
						scoreboard.utilities.voteCounterULSmall)).build ();

		Text counter = TextBuilder
				.create ()
				.text ((int) (Math.ceil ((nrOfCalled + 1) / 2)) + " / "
						+ scoreboard.utilities.voteMap.size ())
				.fill (Color.WHITE)
				.font (Font.font ("Coolvetica RG", FontWeight.LIGHT, 32))
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
