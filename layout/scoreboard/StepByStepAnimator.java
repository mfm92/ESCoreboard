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
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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
		
		ScaleTransition scTransPV = new ScaleTransition (scoreboard.getVoteTokenDuration ().divide (5d/3d));
		scTransPV.setNode (pointView);
		scTransPV.setByX (((double) scoreboard.getFlagWidth () - (double) pointView.getWidth ()) / (double) pointView.getWidth ());
		scTransPV.setByY (((double) scoreboard.getFlagHeight () - (double) pointView.getHeight ()) / (double) pointView.getHeight ());
		
		KeyFrame dZ = new KeyFrame (Duration.ZERO,
				new KeyValue (pointView.xProperty (), pointView.getX () - nationGroupL.getLayoutX ()),
				new KeyValue (pointView.yProperty (), pointView.getY () - nationGroupL.getLayoutY ()));
		
		KeyFrame dF = new KeyFrame (scoreboard.getVoteTokenDuration ().divide (5d/3d),
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
				tTrans.setDuration (scoreboard.getVoteTokenDuration().subtract (Duration.seconds (0.7)));
				tTrans.setDelay (Duration.seconds (0.45));
				tTrans.setByX (xShift);
				tTrans.setByY (yShift);
				tTrans.setAutoReverse (false);
				tTrans.setInterpolator (Interpolator.EASE_BOTH);
				tTrans.setCycleCount (1);

				transTrans.add (tTrans);
			}
			
			Timeline opacityT = new Timeline();
			Group target = scoreboard.getGroupNationMap ().get (receiver);
			
			Rectangle r = new Rectangle (scoreboard.getScoreboardHeight() / sizeDenom, scoreboard.getScoreboardHeight() / sizeDenom);
			r.setFill (Color.WHITE);
			r.setOpacity (0.7);
			r.setEffect (new MotionBlur (20, 20));
			r.setLayoutX (0);
			target.getChildren ().add (1, r);
			
			Timeline moveR = new Timeline();
			moveR.getKeyFrames().addAll(new KeyFrame (Duration.seconds(0.45),
							new KeyValue (r.layoutXProperty (), scoreboard.getColumnWidth () - r.getWidth ())),
							
							new KeyFrame (scoreboard.getVoteTokenDuration ().subtract(Duration.seconds(0.45)),
								new KeyValue (r.layoutXProperty (), scoreboard.getColumnWidth () - r.getWidth ())),
								
							new KeyFrame (scoreboard.getVoteTokenDuration(),
								new KeyValue (r.layoutXProperty (), 0)));
			
			
			KeyFrame start = new KeyFrame (Duration.seconds(0.45), new KeyValue (target.opacityProperty (), 1));
			KeyFrame startOn = new KeyFrame (scoreboard.getVoteTokenDuration ().subtract(Duration.seconds(0.9)).divide (5).add(Duration.seconds(0.45)), new KeyValue (target.opacityProperty (), 0));
			KeyFrame endOut = new KeyFrame (scoreboard.getVoteTokenDuration ().subtract(Duration.seconds(0.9)).divide (8d/7d).add(Duration.seconds(0.45)), new KeyValue (target.opacityProperty (), 0));
			KeyFrame end = new KeyFrame (scoreboard.getVoteTokenDuration ().subtract(Duration.seconds(0.45)), new KeyValue (target.opacityProperty (), 1));
						
			Rectangle base = ((Rectangle)(scoreboard.getGroupNationMap ().get (receiver).lookup("#base")));
			Rectangle ptsBase = ((Rectangle)(scoreboard.getGroupNationMap ().get (receiver).lookup("#pointBase")));
			Text nationText = ((Text)(scoreboard.getGroupNationMap ().get (receiver).lookup ("#nationName")));
			Text ptsBaseText = ((Text)(scoreboard.getGroupNationMap ().get (receiver).lookup ("#scoreTest")));
			
			Image newFill = overview.getPosition (standings, receiver) <= scoreboard.getSpecialBorder () ?
					scoreboard.getDataCarrier ().nationTileBackgroundPQScored : scoreboard.getDataCarrier ().nationTileBackgroundScored;
			
			Image newFillPts = overview.getPosition (standings, receiver) <= scoreboard.getSpecialBorder () ?
						scoreboard.getDataCarrier ().pointsTileBackgroundPQ : scoreboard.getDataCarrier ().pointsTileBackground;
						
			Color newColor = overview.getPosition (standings, receiver) <= scoreboard.getSpecialBorder () ? Color.WHITE : Color.BLACK;
			Color newColorScore = overview.getPosition (standings, receiver) <= scoreboard.getSpecialBorder () ? Color.WHITE : Color.BLACK;
			
			KeyFrame endR = new KeyFrame (scoreboard.getVoteTokenDuration ().divide (4),
					new KeyValue (base.fillProperty (), base.getFill ()),
					new KeyValue (nationText.fillProperty (), nationText.getFill ()));
			
			KeyFrame endRNew = new KeyFrame (scoreboard.getVoteTokenDuration ().divide (4d/2d),
					new KeyValue (base.fillProperty (), new ImagePattern (newFill)),
					new KeyValue (nationText.fillProperty (), newColor),
					new KeyValue (ptsBase.fillProperty (), new ImagePattern (newFillPts)),
					new KeyValue (ptsBaseText.fillProperty (), newColorScore));	
			
			opacityT.getKeyFrames ().addAll (start, startOn, endOut, end, endR, endRNew);
			
			moveR.play ();
			opacityT.play ();
			
			final int save = ++scoreboard.inCountryCounter;

			for (TranslateTransition tT : transTrans) {
				tT.play ();
				if (tT == transTrans.get (transTrans.size () - 1)) {
					moveR.setOnFinished (eventTTFinished -> {
						Collections.sort (scoreboard.getParticipants());								
						scoreboard.getTileUpdater().updateTiles (scoreboard, receiver);

						// GET RID OF THAT
						scoreboard.getGroupNationMap()
								.get (receiver)
								.getChildren ()
								.remove (scoreboard.getGroupNationMap().get (receiver).lookup ("#tmpPts"));
						
						Timeline pause = new Timeline();
						pause.getKeyFrames().add (new KeyFrame
								(Duration.seconds(3), 
										new KeyValue (scoreboard.getGroupNationMap ().get (receiver).layoutXProperty (), 
										scoreboard.getGroupNationMap ().get (receiver).getLayoutX ())));

						pause.play ();
						pause.setOnFinished (eventPause -> {
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
					});
				}
			}
		});
	}
}
