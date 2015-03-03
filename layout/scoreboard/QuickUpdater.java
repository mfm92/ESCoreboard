package scoreboard;

import java.util.ArrayList;
import java.util.Collections;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

		scoreboard.root.getChildren ().remove (
				scoreboard.root.lookup ("#media"));
		final ArrayList<TranslateTransition> transTrans = new ArrayList<> ();
		int sizeDenom = (scoreboard.participants.size () + 1) / scoreboard.getColumnsNr();

		// CLEAR AFTER FULL VOTE
		votesClear (voter, scoreboard);
		scoreboard.tileUpdater.updateBackgroundOnly (scoreboard);
		final Votes votes = scoreboard.utilities.voteMap.get (voter);
		ArrayList<Timeline> timelines = new ArrayList<> ();

		for (int i = 1; i <= scoreboard.getTransParts(); i++) {
			// PLACE POINT NODE
			Rectangle pointView = scoreboard.pointViews.get ((i - 1));
			pointView.setHeight (0.7*(scoreboard.getHeight()/sizeDenom));
			Participant receiver = votes.getReceivers ()[(i - 1)];
			receiver.setTmpScore (scoreboard.indicesToPoints (i - 1));
			receiver.setScoredFlag (true);
			Group nationGroup = scoreboard.groupNationMap.get (receiver);
			pointView.setId ("tmpPts");
			scoreboard.root.getChildren ().remove (pointView);
			nationGroup.getChildren ().remove (pointView);
			nationGroup.getChildren ().add (pointView);

			double newPosX = getXCoordByPos (
					overview.getPosition (oldStandings, receiver), scoreboard) + 0.15*scoreboard.getHeight()/sizeDenom;
			double newPosY = getYCoordByPos (
					overview.getPosition (oldStandings, receiver), scoreboard) + 0.15*scoreboard.getHeight()/sizeDenom;
			double oldPosX = pointView.getX ();
			double oldPosY = pointView.getY ();

			Timeline timeline = new Timeline ();
			timeline.getKeyFrames ().addAll (
					new KeyFrame (Duration.ZERO, new KeyValue (
							pointView.xProperty (), oldPosX
									- nationGroup.getLayoutX ()), new KeyValue (
							pointView.yProperty (), oldPosY
									- nationGroup.getLayoutY ())),
					new KeyFrame (scoreboard.voteTokenDuration,
							new KeyValue (pointView.xProperty (), newPosX
									- nationGroup.getLayoutX ()), new KeyValue (
									pointView.yProperty (), newPosY
											- nationGroup.getLayoutY ())));

			scoreboard.groupNationMap.get (scoreboard.participants.get (sizeDenom - 2)).toFront ();
			nationGroup.toFront ();
			timelines.add (timeline);
		}

		scoreboard.root.getChildren ().remove (scoreboard.root.lookup ("#To7"));
		for (Timeline timeline : timelines) {
			timeline.play ();
			if (timeline == timelines.get (timelines.size () - 1)) {
				timeline.setOnFinished (new EventHandler<ActionEvent> () {
					@Override
					public void handle(ActionEvent event) {
						// UPDATE
						scoreboard.tileUpdater.updateTiles (scoreboard, null);

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

							TranslateTransition tTrans = new TranslateTransition ();
							
							tTrans.setNode (nationGroup);
							tTrans.setDuration (scoreboard.voteTokenDuration);
							tTrans.setByX (xShift);
							tTrans.setByY (yShift);
							tTrans.setAutoReverse (false);
							tTrans.setInterpolator (Interpolator.EASE_BOTH);
							tTrans.setCycleCount (1);

							transTrans.add (tTrans);
						}
						for (TranslateTransition tT : transTrans) {
							for (int i = 0; i < scoreboard.getTransParts(); i++) {
								scoreboard.pointViews.get (i).toFront ();
							}
							tT.play ();
							if (tT == transTrans.get (transTrans.size () - 1)) {
								tT.setOnFinished (new EventHandler<ActionEvent> () {
									@Override
									public void handle(ActionEvent arg0) {
										Collections.sort (scoreboard.participants);
										scoreboard.tileUpdater.updateTiles (scoreboard, null);
										
										for (int i = 0; i < scoreboard.getTransParts(); i++) {
											scoreboard.root
													.getChildren ()
													.remove (scoreboard.pointViews.get (i));
										}

										// NEXT VOTES, PLEASE...
										Platform.runLater (new VoteAdder (
												overview, scoreboard,
												scoreboard.utilities,
												++scoreboard.inCountryCounter, tradVP));
										return;
									}
								});
							}
						} //woah... dat staircase
					}
				});
			} // jesus christ
		}
	}
} //never again