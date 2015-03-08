package scoreboard;

import java.util.ArrayList;
import java.util.Collections;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nations.Participant;
import nations.Votes;
import data.Standings;

public class StepByStepAnimator extends UpdateAnimator {

	@Override
	public void updateAnimate(final Scoreboard scoreboard,
			final Participant voter, final Standings overview,
			final ArrayList<Participant> oldStandings,
			final ArrayList<Participant> standings, final boolean tradVP) {
		
		if (scoreboard.inCountryCounter % 10 == 1) {
			scoreboard.getRoot().getChildren ().remove (scoreboard.getRoot().lookup ("#media"));
			votesClear (voter, scoreboard);
			scoreboard.getTileUpdater().updateBackgroundOnly (scoreboard);
		}
		
		final ArrayList<TranslateTransition> transTrans = new ArrayList<> ();
		int sizeDenom = (int) Math.ceil ((double) scoreboard.getParticipants().size () / (double) scoreboard.getColumnsNr());
		
		// PLACE POINT NODE
		Rectangle pointView = scoreboard.getPointViews().get ((scoreboard.inCountryCounter - 1) % 10);
		pointView.setHeight (0.7 * (scoreboard.getHeight()/sizeDenom));
		Votes votes = scoreboard.getDataCarrier().voteMap.get (voter);
		Participant receiver = votes.getReceivers ()[(scoreboard.inCountryCounter - 1) % 10];
		receiver.setTmpScore (scoreboard.indicesToPoints ((scoreboard.inCountryCounter - 1) % 10));
		receiver.setScoredFlag (true);
		Group nationGroupL = scoreboard.getGroupNationMap().get (receiver);
		pointView.setId ("tmpPts");
		scoreboard.getRoot().getChildren ().remove (pointView);
		nationGroupL.getChildren ().remove (pointView);
		nationGroupL.getChildren ().add (pointView);

		double newPosX = getXCoordByPos (
				overview.getPosition (oldStandings, receiver), scoreboard) + 
				(((scoreboard.getHeight() / sizeDenom) - scoreboard.getFlagHeight()) / 2);
		double newPosY = getYCoordByPos (
				overview.getPosition (oldStandings, receiver), scoreboard) + 
				(((scoreboard.getHeight() / sizeDenom) - scoreboard.getFlagHeight()) / 2);
		
		double oldPosX = pointView.getX ();
		double oldPosY = pointView.getY ();

		Timeline timeline = new Timeline ();
		timeline.getKeyFrames ().addAll (
				new KeyFrame (Duration.ZERO, new KeyValue (
						pointView.xProperty (), oldPosX
								- nationGroupL.getLayoutX ()), new KeyValue (
						pointView.yProperty (), oldPosY
								- nationGroupL.getLayoutY ())),
				new KeyFrame (scoreboard.getVoteTokenDuration(), new KeyValue (pointView
						.xProperty (), newPosX - nationGroupL.getLayoutX ()),
						new KeyValue (pointView.yProperty (), newPosY
								- nationGroupL.getLayoutY ())));

		
		timeline.play ();
		
		ArrayList<Participant> parts = new ArrayList<>(scoreboard.getGroupNationMap ().keySet ());
		Collections.sort (parts);
		
		for (Participant p : parts) scoreboard.getGroupNationMap ().get (p).toBack ();
		
		for (int i = 1; i <= scoreboard.getColumnsNr () - 1; i++) {
			scoreboard.getGroupNationMap().get (scoreboard.getParticipants().get (i * sizeDenom - 1)).toFront ();
		}
		
		scoreboard.getGroupNationMap().get (receiver).toFront();

		final Participant rece = receiver;
		timeline.setOnFinished (event -> {
			// COUNT UP SCORE
			countUpScore (scoreboard, rece);

			// MOVE TILES
			for (Participant participant : scoreboard.getParticipants()) {
				int oldPos = overview.getPosition (oldStandings, participant);
				int newPos = overview.getPosition (standings, participant);

				double oldX = getXCoordByPos (oldPos, scoreboard);
				double oldY = getYCoordByPos (oldPos, scoreboard);
				double newX = getXCoordByPos (newPos, scoreboard);
				double newY = getYCoordByPos (newPos, scoreboard);

				double xShift = newX - oldX;
				double yShift = newY - oldY;

				Group nationGroup = scoreboard.getGroupNationMap ().get (participant);
				TranslateTransition tTrans = new TranslateTransition ();
				
				tTrans.setNode (nationGroup);
				tTrans.setDuration (scoreboard.getVoteTokenDuration());
				tTrans.setByX (xShift);
				tTrans.setByY (yShift);
				tTrans.setAutoReverse (false);
				tTrans.setInterpolator (Interpolator.EASE_BOTH);
				tTrans.setCycleCount (1);

				transTrans.add (tTrans);
			}
			final int save = ++scoreboard.inCountryCounter;

			for (TranslateTransition tT : transTrans) {
				tT.play ();
				if (tT == transTrans.get (transTrans.size () - 1)) {
					tT.setOnFinished (eventTTFinished -> {
						Collections.sort (scoreboard.getParticipants());								
						scoreboard.getTileUpdater().updateTiles (scoreboard, rece);

						// GET RID OF THAT
						scoreboard.getGroupNationMap()
								.get (rece)
								.getChildren ()
								.remove (scoreboard.getGroupNationMap().get (rece).lookup ("#tmpPts"));

						// SHOW 12 POINTER MEZZO
						if (scoreboard.inCountryCounter % 10 == 1
								&& scoreboard.inCountryCounter != 1) {
							Platform.runLater (scoreboard.showAndPraise12Pointer (rece, 
									voter, overview, save, scoreboard));
							return;
						}

						// NEXT VOTES, PLEASE...
						Platform.runLater (new VoteAdder (overview,
								scoreboard, scoreboard.getDataCarrier(), save, tradVP));
					});
				}
			}
		});
	}
}
