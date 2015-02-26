package scoreboard;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import nations.Participant;
import data.Standings;

public abstract class UpdateAnimator {

	public abstract void updateAnimate(final Scoreboard scoreboard,
			final Participant voter, final Standings overview,
			final ArrayList<Participant> oldStandings,
			final ArrayList<Participant> standings, final boolean tradVP);

	void countUpScore(Scoreboard scoreboard, Participant receiver) {
		// SET CORRECT SCORE
		int newNrOfPoints = receiver.getScore ();
		Group nationGroup = scoreboard.groupNationMap.get (receiver);
		double xProperty = nationGroup.getChildren ().get (3).getLayoutX ();
		double yProperty = nationGroup.getChildren ().get (3).getLayoutY ();
		double vHeight = ((VBox) (nationGroup.getChildren ().get (3)))
				.getHeight ();
		double vWidth = ((VBox) (nationGroup.getChildren ().get (3)))
				.getWidth ();

		// COUNT IT UP
		nationGroup.getChildren ().remove (nationGroup.lookup ("#score"));

		Text scoreTest = TextBuilder.create ()
				.text (new Integer (newNrOfPoints).toString ())
				.font (Font.font ("Inconsolata", FontWeight.MEDIUM, 38))
				.fill (Color.WHITE).build ();

		VBox scoreVBox = new VBox ();
		scoreVBox.setLayoutX (xProperty);
		scoreVBox.setLayoutY (yProperty);
		scoreVBox.setPrefHeight (vHeight);
		scoreVBox.setPrefWidth (vWidth);
		scoreVBox.setAlignment (Pos.CENTER);
		scoreVBox.getChildren ().add (scoreTest);

		nationGroup.getChildren ().add (3, scoreVBox);
	}
	
	void votesClear(Participant voter, Scoreboard scoreboard) {
		scoreboard.currentVoter = voter;
		for (Participant finalist : scoreboard.participants) {
			finalist.setTmpScore (0);
			finalist.setScoredFlag (false);
			scoreboard.groupNationMap
					.get (finalist)
					.getChildren ()
					.remove (scoreboard.groupNationMap.get (finalist).lookup ("#ptsProof"));
		}
		
		if (scoreboard.columnsNr > 2) {
			scoreboard.bottomSideBar.makeSideOfScoreboard (scoreboard.root,
					voter, scoreboard, scoreboard.bottomBarWidth, scoreboard.bottomBarX);
		} else {
			scoreboard.rightSideBar.makeSideOfScoreboard (scoreboard.root, voter, scoreboard, 
					scoreboard.height, scoreboard.globalYOffset);
		}
	}

	double getXCoordByPos(int position, Scoreboard scoreboard) {
		return scoreboard.globalXOffset + scoreboard.columnWidth * 
				((position - 1) / ((int) Math.ceil ((double) scoreboard.participants.size () / (double) scoreboard.columnsNr)));
	}

	double getYCoordByPos(int position, Scoreboard scoreboard) {
		return scoreboard.globalYOffset + (scoreboard.height / ((int) Math.ceil ((double) scoreboard.participants.size () / (double) scoreboard.columnsNr)))
				* ((position - 1) % ((int) Math.ceil ((double) scoreboard.participants.size () / (double) scoreboard.columnsNr)));
	}
}