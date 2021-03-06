package scoreboard;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
		Group nationGroup = scoreboard.getGroupNationMap().get (receiver);
		double xProperty = nationGroup.getChildren ().get (3).getLayoutX ();
		double yProperty = nationGroup.getChildren ().get (3).getLayoutY ();
		double vHeight = ((VBox) (nationGroup.getChildren ().get (3))).getHeight ();
		double vWidth = ((VBox) (nationGroup.getChildren ().get (3))).getWidth ();

		// COUNT IT UP
		nationGroup.getChildren ().remove (nationGroup.lookup ("#score"));

		Text scoreTest = new Text();
		
		scoreTest.setText (new Integer (newNrOfPoints).toString ());
		scoreTest.setFont (Font.font (scoreboard.getDataCarrier().font_1, FontWeight.MEDIUM, 34 * (scoreboard.getScreenWidth () / 1920d)));
		scoreTest.setId ("scoreTest");
		scoreTest.setFill (Color.WHITE);

		VBox scoreVBox = new VBox ();
		scoreVBox.setLayoutX (xProperty);
		scoreVBox.setLayoutY (yProperty);
		scoreVBox.setPrefHeight (vHeight);
		scoreVBox.setPrefWidth (vWidth);
		scoreVBox.setAlignment (Pos.CENTER);
		scoreVBox.getChildren ().add (scoreTest);

		nationGroup.getChildren ().add (3, scoreVBox);
	}
	
	void votesClear(Participant voter, Participant next, Scoreboard scoreboard) {
		scoreboard.setCurrentVoter (voter);
		for (Participant finalist : scoreboard.getParticipants()) {
			finalist.setTmpScore (0);
			finalist.setScoredFlag (false);
			scoreboard.getGroupNationMap()
					.get (finalist)
					.getChildren ()
					.remove (scoreboard.getGroupNationMap().get (finalist).lookup ("#ptsProof"));
		}
		
		(scoreboard.getColumnsNr () > 2 ? scoreboard.getBottomSideBar() : scoreboard.getRightSideBar ()).
			makeSideOfScoreboard (scoreboard.getRoot (), voter, next, scoreboard);
	}

	static double getXCoordByPos(int position, Scoreboard scoreboard) {
		return (((double) scoreboard.getScreenWidth() - (double) scoreboard.getBackgroundWidth()) / 2d
				+ scoreboard.getWidthFromLeftOffset () 
				+ scoreboard.getColumnWidth() * 
					((position - 1) / 
					((int) Math.ceil ((double) scoreboard.getParticipants().size () / 
							(double) scoreboard.getColumnsNr()))));
	}

	static double getYCoordByPos(int position, Scoreboard scoreboard) {
		return (scoreboard.getHeightFromTopOffset () 
				+ ((double) scoreboard.getScreenHeight () - (double) scoreboard.getBackgroundHeight ()) / 2d 
				+ (scoreboard.getScoreboardHeight() 
						/ ((int) Math.ceil ((double) scoreboard.getParticipants().size () / 
								(double) scoreboard.getColumnsNr()))) * 
								((position - 1) % 
										((int) Math.ceil ((double) scoreboard.getParticipants().size () / 
												(double) scoreboard.getColumnsNr()))));
	}
}