package scoreboard;

import java.util.ArrayList;
import java.util.Collections;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
			scoreboard.root.getChildren ().remove (scoreboard.root.lookup ("#media"));
			votesClear (voter, scoreboard);
			scoreboard.tileUpdater.updateBackgroundOnly (scoreboard);
		}
		
		final ArrayList<TranslateTransition> transTrans = new ArrayList<> ();
		int sizeDenom = (int) Math.ceil ((double) scoreboard.participants.size () / (double) scoreboard.columnsNr);
		
		// PLACE POINT NODE
		Rectangle pointView = scoreboard.pointViews.get ((scoreboard.inCountryCounter - 1) % 10);
		pointView.setHeight (0.7 * (scoreboard.height/sizeDenom));
		Votes votes = scoreboard.utilities.voteMap.get (voter);
		Participant receiver = votes.getReceivers ()[(scoreboard.inCountryCounter - 1) % 10];
		receiver.setTmpScore (scoreboard.indicesToPoints ((scoreboard.inCountryCounter - 1) % 10));
		receiver.setScoredFlag (true);
		Group nationGroup = scoreboard.groupNationMap.get (receiver);
		pointView.setId ("tmpPts");
		scoreboard.root.getChildren ().remove (pointView);
		nationGroup.getChildren ().remove (pointView);
		nationGroup.getChildren ().add (pointView);

		double newPosX = getXCoordByPos (
				overview.getPosition (oldStandings, receiver), scoreboard) + 
				(((scoreboard.height / sizeDenom) - scoreboard.flagHeight) / 2);
		double newPosY = getYCoordByPos (
				overview.getPosition (oldStandings, receiver), scoreboard) + 
				(((scoreboard.height / sizeDenom) - scoreboard.flagHeight) / 2);
		
		double oldPosX = pointView.getX ();
		double oldPosY = pointView.getY ();

		Timeline timeline = new Timeline ();
		timeline.getKeyFrames ().addAll (
				new KeyFrame (Duration.ZERO, new KeyValue (
						pointView.xProperty (), oldPosX
								- nationGroup.getLayoutX ()), new KeyValue (
						pointView.yProperty (), oldPosY
								- nationGroup.getLayoutY ())),
				new KeyFrame (new Duration (1500), new KeyValue (pointView
						.xProperty (), newPosX - nationGroup.getLayoutX ()),
						new KeyValue (pointView.yProperty (), newPosY
								- nationGroup.getLayoutY ())));

		scoreboard.groupNationMap.get (scoreboard.participants.get (sizeDenom - 1)).toFront ();		
		timeline.play ();

		final Participant rece = receiver;
		timeline.setOnFinished (new EventHandler<ActionEvent> () {
			@Override
			public void handle(ActionEvent event) {
				// COUNT UP SCORE
				countUpScore (scoreboard, rece);

				// MOVE TILES
				for (Participant participant : scoreboard.participants) {
					int oldPos = overview.getPosition (oldStandings, participant);
					int newPos = overview.getPosition (standings, participant);

					double oldX = getXCoordByPos (oldPos, scoreboard);
					double oldY = getYCoordByPos (oldPos, scoreboard);
					double newX = getXCoordByPos (newPos, scoreboard);
					double newY = getYCoordByPos (newPos, scoreboard);

					double xShift = newX - oldX;
					double yShift = newY - oldY;

					Group nationGroup = scoreboard.groupNationMap
							.get (participant);

					TranslateTransition tTrans = TranslateTransitionBuilder
							.create ().node (nationGroup)
							.duration (new Duration (1500)).byX (xShift)
							.byY (yShift).autoReverse (false)
							.interpolator (Interpolator.EASE_BOTH)
							.cycleCount (1).build ();

					transTrans.add (tTrans);
				}
				final int save = ++scoreboard.inCountryCounter;

				for (TranslateTransition tT : transTrans) {
					tT.play ();
					if (tT == transTrans.get (transTrans.size () - 1)) {
						tT.setOnFinished (new EventHandler<ActionEvent> () {
							@Override
							public void handle(ActionEvent event) {
								Collections.sort (scoreboard.participants);								
								scoreboard.tileUpdater.updateTiles (scoreboard, rece);

								// GET RID OF THAT
								scoreboard.groupNationMap
										.get (rece)
										.getChildren ()
										.remove (scoreboard.groupNationMap.get (rece).lookup ("#tmpPts"));

								// SHOW 12 POINTER MEZZO
								if (scoreboard.inCountryCounter % 10 == 1
										&& scoreboard.inCountryCounter != 1) {
									Platform.runLater (scoreboard.showAndPraise12Pointer (rece,
													voter, overview, save, scoreboard));
									return;
								}

								// NEXT VOTES, PLEASE...
								Platform.runLater (new VoteAdder (overview,
										scoreboard, scoreboard.utilities, save, tradVP));
							}
						});
					}
				}
			}
		});
	}
}
