package scoreboard;

import java.util.ArrayList;
import java.util.Collections;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
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
		
		ArrayList<TranslateTransition> transTrans = new ArrayList<> ();
		int sizeDenom = (int) Math.ceil ((double) scoreboard.getParticipants().size () / (double) scoreboard.getColumnsNr());
		
		// PLACE POINT NODE
		Rectangle pointView = scoreboard.getPointViews().get ((scoreboard.inCountryCounter - 1) % 10);
		pointView.setId ("tmpPts");
		pointView.toFront ();
		
		Votes votes = scoreboard.getDataCarrier().voteMap.get (voter);
		
		Participant receiver = votes.getReceivers ()[(scoreboard.inCountryCounter - 1) % 10];
		receiver.setTmpScore (scoreboard.indicesToPoints ((scoreboard.inCountryCounter - 1) % 10));
		receiver.setScoredFlag (true);
		
		Group nationGroupL = scoreboard.getGroupNationMap().get (receiver);
		scoreboard.getRoot().getChildren ().remove (pointView);
		nationGroupL.getChildren ().remove (pointView);
		nationGroupL.getChildren ().add (pointView);
		
		ScaleTransition scTransPV = new ScaleTransition (scoreboard.getVoteTokenDuration ());
		scTransPV.setNode (pointView);
		scTransPV.setByX (((double) scoreboard.getFlagWidth () - (double) pointView.getWidth ()) / (double) pointView.getWidth ());
		scTransPV.setByY (((double) scoreboard.getFlagHeight () - (double) pointView.getHeight ()) / (double) pointView.getHeight ());
		
		KeyFrame dZ = new KeyFrame (Duration.ZERO,
				new KeyValue (pointView.xProperty (), pointView.getX () - nationGroupL.getLayoutX ()),
				new KeyValue (pointView.yProperty (), pointView.getY () - nationGroupL.getLayoutY ()));
		
		KeyFrame dF = new KeyFrame (scoreboard.getVoteTokenDuration (),
				new KeyValue (pointView.xProperty (), nationGroupL.lookup ("#icon").getLayoutX () +
						((pointView.getWidth () * (scTransPV.getByX () + 1) - pointView.getWidth ()) / 2)),
				new KeyValue (pointView.yProperty (), nationGroupL.lookup ("#icon").getLayoutY () +
						(pointView.getHeight () * (scTransPV.getByY () + 1) - pointView.getHeight ()) / 2));

		Timeline timeline = new Timeline ();
		timeline.getKeyFrames ().addAll (dZ, dF);
		timeline.play ();
		
		ParallelTransition pTrans = new ParallelTransition (scTransPV);
		pTrans.play ();
		
		ArrayList<Participant> parts = new ArrayList<>(scoreboard.getGroupNationMap ().keySet ());
		Collections.sort (parts);
		
		for (Participant p : parts) scoreboard.getGroupNationMap ().get (p).toBack ();
		
		for (int i = 1; i <= scoreboard.getColumnsNr () - 1; i++) {
			scoreboard.getGroupNationMap().get (scoreboard.getParticipants().get (i * sizeDenom - 1)).toFront ();
		}
		scoreboard.getGroupNationMap().get (receiver).toFront();

		pTrans.setOnFinished (event -> {
			scoreboard.getRoot().getChildren ().remove (pointView);	
			
			// COUNT UP SCORE
			countUpScore (scoreboard, receiver);

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
						scoreboard.getTileUpdater().updateTiles (scoreboard, receiver);

						// GET RID OF THAT
						scoreboard.getGroupNationMap()
								.get (receiver)
								.getChildren ()
								.remove (scoreboard.getGroupNationMap().get (receiver).lookup ("#tmpPts"));

						// SHOW 12 POINTER MEZZO
						if (scoreboard.inCountryCounter % 10 == 1
								&& scoreboard.inCountryCounter != 1) {
							Platform.runLater (scoreboard.showAndPraise12Pointer (receiver, 
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
