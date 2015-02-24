package scoreboard;

import java.util.ArrayList;
import java.util.Collections;

import data.Standings;
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

public class OneToSevenUpdateAnimator extends UpdateAnimator {

	@Override
	public void updateAnimate(final Scoreboard scoreboard,
			final Participant voter, final Standings overview,
			final ArrayList<Participant> oldStandings,
			final ArrayList<Participant> standings) {

		scoreboard.root.getChildren ().remove (
				scoreboard.root.lookup ("#media"));
		final ArrayList<TranslateTransition> transTrans = new ArrayList<> ();
		int sizeDenom = (scoreboard.participants.size () + 1) / scoreboard.columnsNr;

		// CLEAR AFTER FULL VOTE
		votesClear (voter, scoreboard);
		scoreboard.tileUpdater.updateBackgroundOnly (scoreboard);
		final Votes votes = scoreboard.utilities.voteMap.get (voter);
		ArrayList<Timeline> timelines = new ArrayList<> ();

		for (int i = 1; i <= 7; i++) {
			// PLACE POINT NODE
			Rectangle pointView = scoreboard.pointViews.get ((i - 1));
			pointView.setHeight (0.7*(840/sizeDenom));
			Participant receiver = votes.getReceivers ()[(i - 1)];
			receiver.setTmpScore (indicesToPoints (i - 1));
			receiver.setScoredFlag (true);
			Group nationGroup = scoreboard.groupNationMap.get (receiver);
			pointView.setId ("tmpPts");
			scoreboard.root.getChildren ().remove (pointView);
			nationGroup.getChildren ().remove (pointView);
			nationGroup.getChildren ().add (pointView);

			double newPosX = getXCoordByPos (
					overview.getPosition (oldStandings, receiver), scoreboard) + 0.15*840/sizeDenom;
			double newPosY = getYCoordByPos (
					overview.getPosition (oldStandings, receiver), scoreboard) + 0.15*840/sizeDenom;
			double oldPosX = pointView.getX ();
			double oldPosY = pointView.getY ();

			Timeline timeline = new Timeline ();
			timeline.getKeyFrames ().addAll (
					new KeyFrame (Duration.ZERO, new KeyValue (
							pointView.xProperty (), oldPosX
									- nationGroup.getLayoutX ()), new KeyValue (
							pointView.yProperty (), oldPosY
									- nationGroup.getLayoutY ())),
					new KeyFrame (new Duration (1500),
							new KeyValue (pointView.xProperty (), newPosX
									- nationGroup.getLayoutX ()), new KeyValue (
									pointView.yProperty (), newPosY
											- nationGroup.getLayoutY ())));

			scoreboard.groupNationMap.get (scoreboard.participants.get (sizeDenom - 1)).toFront ();
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
						scoreboard.tileUpdater.updateTiles (scoreboard);

						// MOVE TILES
						for (Participant participant : scoreboard.participants) {
							int oldPos = overview.getPosition (oldStandings,
									participant);
							int newPos = overview.getPosition (standings,
									participant);

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
									.duration (new Duration (1500))
									.byX (xShift).byY (yShift)
									.autoReverse (false)
									.interpolator (Interpolator.EASE_BOTH)
									.cycleCount (1).build ();

							transTrans.add (tTrans);
						}
						for (TranslateTransition tT : transTrans) {
							for (int i = 0; i < 7; i++) {
								scoreboard.pointViews.get (i).toFront ();
							}
							tT.play ();
							if (tT == transTrans.get (transTrans.size () - 1)) {
								tT.setOnFinished (new EventHandler<ActionEvent> () {
									@Override
									public void handle(ActionEvent arg0) {
										Collections
												.sort (scoreboard.participants);
										scoreboard.tileUpdater
												.updateTiles (scoreboard);
										for (int i = 0; i < 7; i++) {
											scoreboard.root
													.getChildren ()
													.remove (
															scoreboard.pointViews
																	.get (i));
										}

										// NEXT VOTES, PLEASE...
										Platform.runLater (new VoteAdder (
												overview, scoreboard,
												scoreboard.utilities,
												++scoreboard.inCountryCounter));
										return;
									}
								});
							}
						}
					}
				});
			}
		}
	}

	void votesClear(Participant voter, Scoreboard scoreboard) {
		scoreboard.currentVoter = voter;
		for (Participant finalist : scoreboard.participants) {
			finalist.setTmpScore (0);
			finalist.setScoredFlag (false);
			scoreboard.groupNationMap
					.get (finalist)
					.getChildren ()
					.remove (
							scoreboard.groupNationMap.get (finalist).lookup (
									"#ptsProof"));
		}

		scoreboard.voteSideBarCreator.makeSideOfScoreboard (scoreboard.root,
				voter, scoreboard);
	}

	int indicesToPoints(int index) {
		if (index == 0)
			return 12;
		if (index >= 1 && index <= 8)
			return index;
		if (index == 9)
			return 10;

		System.out.println ("IndicesToPoints returns sth baaaad, index was: "
				+ index);

		return 103859; // absolutely pointless huehue
	}
}
