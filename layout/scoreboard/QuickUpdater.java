package scoreboard;

import java.util.ArrayList;
import java.util.Collections;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
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

public class QuickUpdater extends UpdateAnimator {

	@Override
	public void updateAnimate(final Scoreboard scoreboard,
			final Participant voter, final Standings overview,
			final ArrayList<Participant> oldStandings,
			final ArrayList<Participant> standings, final boolean tradVP) {

		scoreboard.getRoot().getChildren ().remove (scoreboard.getRoot().lookup ("#media"));
		final ArrayList<TranslateTransition> transTrans = new ArrayList<> ();
		
		scoreboard.getRoot().getChildren().remove (scoreboard.getRoot().lookup ("#To7"));
		
		for (int i = 0; i < scoreboard.getPointViews ().size (); i++) {
			scoreboard.getRoot().getChildren().remove(scoreboard.getPointViews().get(i));
			scoreboard.getPointViews ().get (i).setX (scoreboard.getPointTokenXOffset () + i * scoreboard.getPointTokenWidth ());;
			scoreboard.getPointViews ().get (i).setY (scoreboard.getPointTokenYOffset());
			scoreboard.getRoot().getChildren().add(scoreboard.getPointViews().get(i));
		}

		// CLEAR AFTER FULL VOTE
		Participant nextVoter = ((scoreboard.inCountryCounter + 10) / 10) >= overview.getVotes ().size () ? null :
			overview.getVotes ().get ((scoreboard.inCountryCounter + 10) / 10).getVoter ();
		
		votesClear (voter, nextVoter, scoreboard);
		scoreboard.getTileUpdater().updateBackgroundOnly (scoreboard);
		final Votes votes = scoreboard.getDataCarrier().voteMap.get (voter);
		ArrayList<Timeline> timelines = new ArrayList<> ();
		ArrayList<ScaleTransition> transitions = new ArrayList<>();
		
		for (int i = 1; i <= scoreboard.getTransParts(); i++) {
			// PLACE POINT NODE
			Rectangle pointView = scoreboard.getPointViews().get (i - 1);
			
			Participant receiver = votes.getReceivers ()[(i - 1)];
			receiver.setTmpScore (scoreboard.indicesToPoints (i - 1));
			receiver.setScoredFlag (true);			
			
			Group nationGroupL = scoreboard.getGroupNationMap().get (receiver);
			nationGroupL.getChildren ().add (pointView);
			
			ScaleTransition scTransPV = new ScaleTransition (scoreboard.getVoteTokenDuration ());
			scTransPV.setNode (pointView);
			scTransPV.setByX (((double) scoreboard.getFlagWidth () - (double) pointView.getWidth ()) / (double) pointView.getWidth ());
			scTransPV.setByY (((double) scoreboard.getFlagHeight () - (double) pointView.getHeight ()) / (double) pointView.getHeight ());
			
			KeyFrame dZ = new KeyFrame (Duration.ZERO,
					new KeyValue (pointView.xProperty (), pointView.getX() - nationGroupL.getLayoutX()),
					new KeyValue (pointView.yProperty (), pointView.getY() - nationGroupL.getLayoutY()));
			
			
			KeyFrame dMid = new KeyFrame (scoreboard.getVoteTokenDuration (),
					new KeyValue (pointView.xProperty (), pointView.getX() - nationGroupL.getLayoutX()),
					new KeyValue (pointView.yProperty (), pointView.getY() - nationGroupL.getLayoutY()));
			
			KeyFrame scStart = new KeyFrame (scoreboard.getVoteTokenDuration (),
					new KeyValue (pointView.scaleXProperty (), 1),
					new KeyValue (pointView.scaleYProperty (), 1));
			
			KeyFrame dF = new KeyFrame (scoreboard.getVoteTokenDuration ().add (dMid.getTime ()),
					new KeyValue (pointView.xProperty (), nationGroupL.lookup ("#icon").getLayoutX () +
							((pointView.getWidth () * (scTransPV.getByX () + 1) - pointView.getWidth ()) / 2)),
					new KeyValue (pointView.yProperty (), nationGroupL.lookup ("#icon").getLayoutY () +
							(pointView.getHeight () * (scTransPV.getByY () + 1) - pointView.getHeight ()) / 2));
			
			KeyFrame scEnd = new KeyFrame (scoreboard.getVoteTokenDuration ().add (dMid.getTime ()),
					new KeyValue (pointView.scaleXProperty (), scTransPV.getByX () + 1),
					new KeyValue (pointView.scaleYProperty (), scTransPV.getByY () + 1));

			Timeline timeline = new Timeline ();
			timeline.getKeyFrames ().addAll (dZ, dMid, dF, scStart, scEnd);
			timelines.add (timeline);
			
			transitions.add (scTransPV);
		}
		
		for (Participant p : scoreboard.getParticipants()) scoreboard.getGroupNationMap ().get (p).toBack ();
		
		for (int i = 1; i <= scoreboard.getTransParts (); i++) {
			scoreboard.getGroupNationMap ().get (votes.getReceivers ()[i-1]).toFront ();
		}
		
		for (Timeline timeline : timelines) {
			timeline.play ();
			if (timeline == timelines.get (timelines.size () - 1)) {				
				timeline.setOnFinished (event -> {
					// UPDATE
					scoreboard.getTileUpdater().updateTiles (scoreboard, null);

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

						Group nationGroup = scoreboard.getGroupNationMap().get (participant);

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
					
					for (TranslateTransition tT : transTrans) {
						for (int i = 0; i < scoreboard.getTransParts(); i++) {
							scoreboard.getPointViews().get (i).toFront ();
						}
						tT.play ();
						if (tT == transTrans.get (transTrans.size () - 1)) {
							tT.setOnFinished (eventTT -> {
								Collections.sort (scoreboard.getParticipants());
								scoreboard.getTileUpdater().updateTiles (scoreboard, null);
								
								for (int i = 0; i < scoreboard.getTransParts(); i++) {
									scoreboard.getRoot().getChildren ().remove (scoreboard.getPointViews().get (i));
								}

								// NEXT VOTES, PLEASE...
								Platform.runLater (new VoteAdder (
										overview, scoreboard,
										scoreboard.getDataCarrier(),
										++scoreboard.inCountryCounter, tradVP));
								return;
							});
						}
					} //woah... dat staircase
				});
			} // jesus christ
		}
	}
} //never again