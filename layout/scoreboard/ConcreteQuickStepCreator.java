package scoreboard;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nations.Entry;
import nations.Participant;
import data.Standings;

public class ConcreteQuickStepCreator extends IntermediatePreparator {

	@Override
	public void showSplitScreen(final Scoreboard scoreboard,
			final Standings standings, final boolean tradVP) {
		clear (scoreboard.getRoot());
		createPointsArray (scoreboard);

		final Group to7Group = new Group ();
		to7Group.setId ("To7");
		
		if (!tradVP) {
			scoreboard.getRoot().getChildren ().add (to7Group);	
		}

		final Participant currentVoterCopy = standings.getVotes ()
				.get (scoreboard.inCountryCounter / 10).getVoter ();

		Rectangle iR = new Rectangle();
		iR.setWidth (1920);
		iR.setHeight (1080);
		iR.setId ("background");
		iR.setId ("redBack");
		iR.setFill (new ImagePattern (scoreboard.getDataCarrier().backgroundBlue));
		to7Group.getChildren ().add (iR);

		int entryX = 100;
		int entryY = 100;
		
		scoreboard.getRightSideBar().makeSideOfScoreboard (to7Group, currentVoterCopy, scoreboard, 900, entryX);

		if (!tradVP) {
			final ArrayList<Rectangle> rects = new ArrayList<> ();
			final ArrayList<ImageView> flags = new ArrayList<> ();
			final ArrayList<VBox> recTexts = new ArrayList<> ();

			Entry recEntry = currentVoterCopy.getEntry ();
			Media entry = recEntry.getMedia ();
			MediaPlayer entryPlayer = new MediaPlayer (entry);
			entryPlayer.setStartTime (Duration.seconds (recEntry.getStartDuration ()));
			entryPlayer.setStopTime (Duration.seconds (recEntry.getStopDuration () - 4));
			entryPlayer.setAutoPlay (true);
			entryPlayer.setVolume (0);
			entryPlayer.setCycleCount (1);

			MediaView entryView = new MediaView ();
			entryView.setMediaPlayer (entryPlayer);
			entryView.setX (entryX);
			entryView.setY (entryY);
			entryView.setId ("media");
			entryView.setFitHeight (2000);
			entryView.setFitWidth (1200);
			scoreboard.getRoot().getChildren ().add (entryView);
			entryPlayer.play ();

			entryPlayer.setOnPlaying (() -> {
				int transSNum = scoreboard.getTransParts() / scoreboard.getColumnsNrTransition() + 1;
				int transSDen = (int) ((double) scoreboard.getHeightTransition() / (double) transSNum);
				
				for (int i = 0; i < scoreboard.getTransParts(); i++) {
					Rectangle rect = new Rectangle();
					rect.setHeight (transSDen);
					rect.setWidth (scoreboard.getColumnWidthTransition());
					rect.setX (scoreboard.getTransitionXOffset() - scoreboard.getColumnWidthTransition() * ((i+1) / transSNum));
					rect.setY (scoreboard.getTransitionYOffset() - transSDen * ((i+1) % transSNum));
					rect.setFill (new ImagePattern (scoreboard.getDataCarrier().nationTileBackground));
					rects.add (rect);

					ImageView viewFlag = new ImageView();
					viewFlag.setFitHeight (scoreboard.getFlagHeightTransition ());
					viewFlag.setFitWidth (scoreboard.getFlagWidthTransition ());
					viewFlag.setImage (currentVoterCopy.getVotes ().getReceivers ()[i].getFlag ());
					viewFlag.setX (rect.getX () + scoreboard.getPointTokenWidthTransition() + scoreboard.getFlagFromPTOffsetTrans() +
									scoreboard.getPtfromEdgeOffsetTrans());
					viewFlag.setY (rect.getY () + (rect.getHeight () - scoreboard.getFlagHeightTransition()) / 2);
					
					flags.add (viewFlag);
					
					VBox rectVBox = new VBox();
					rectVBox.setLayoutX (viewFlag.getX () + viewFlag.getFitWidth () + scoreboard.getTextFromFlagOffsetTrans());
					rectVBox.setLayoutY (rect.getY ());
					rectVBox.setPrefHeight (transSDen);
					rectVBox.setPrefWidth (rect.getWidth () - (scoreboard.getPointTokenWidthTransition() + 
							scoreboard.getPtfromEdgeOffsetTrans() +
							scoreboard.getFlagFromPTOffsetTrans() +
							scoreboard.getFlagWidthTransition() +
							scoreboard.getTextFromFlagOffsetTrans()));
					rectVBox.setAlignment (Pos.CENTER_LEFT);

					Text recText = new Text();
					recText.setText (currentVoterCopy.getVotes ().getReceivers ()[i].getName ());
					recText.setFont (Font.font ("Coolvetica RG", FontWeight.MEDIUM, 33));
					recText.setFill (Color.WHITE);
					
					rectVBox.getChildren().add (recText);
					recTexts.add (rectVBox);
				}

				final ArrayList<Timeline> timelines = new ArrayList<> ();
				final ArrayList<ImageView> pointViews = new ArrayList<> ();
				
				for (int i = 0; i < scoreboard.getTransParts(); i++) {
					ImageView pointView = new ImageView();
					pointView.setFitHeight (scoreboard.getPointTokenHeightTransition ());
					pointView.setFitWidth (scoreboard.getPointTokenWidthTransition ());
					pointView.setImage (scoreboard.getDataCarrier ().getPointsTokens ().get (i));
					pointView.setX(scoreboard.getPointTokenXOffsetTransition() + i *
							scoreboard.getPointTokenWidthTransition());
					pointView.setY (scoreboard.getPointTokenYOffsetTransition());

					pointViews.add (pointView);
					double shiftPVX = rects.get (i).getX () - pointView.getX () + scoreboard.getPtfromEdgeOffsetTrans();
					double shiftPVY = rects.get (i).getY () - pointView.getY () + (transSDen - scoreboard.getPointTokenHeightTransition()) / 2;
					
					to7Group.getChildren ().add (pointView);
					Timeline timeline = new Timeline ();
					timeline.getKeyFrames ().addAll (
							new KeyFrame (scoreboard.getVoteTokenDuration(), new KeyValue (
									pointView.translateXProperty (), shiftPVX),
									new KeyValue (pointView.translateYProperty (), shiftPVY)));
					timelines.add (timeline);
				}

				int counter = 0;
				for (final Timeline timeline : timelines) {
					timeline.setDelay (Duration.seconds (counter));
					timeline.play ();
					final int cSave = counter++;
					timeline.setOnFinished (event -> {
						to7Group.getChildren ().add (1, rects.get (cSave));
						to7Group.getChildren ().add (flags.get (cSave));
						to7Group.getChildren ().add (recTexts.get (cSave));
						pointViews.get (cSave).toFront ();
					});
				}
			});
			
			entryPlayer.setOnEndOfMedia (() -> {
				final int save = scoreboard.inCountryCounter += (scoreboard.getTransParts() - 1);
				Platform.runLater (new VoteAdder (standings, scoreboard,
						scoreboard.getDataCarrier(), save, tradVP));
			});
			
		} else {
			final int save = scoreboard.inCountryCounter;
			Platform.runLater (new VoteAdder (standings, scoreboard,
					scoreboard.getDataCarrier(), save, tradVP));
		}
	}

	private void clear(Group root) {
		root.getChildren ().remove (root.lookup ("#12Strip"));
		root.getChildren ().remove (root.lookup ("#12Content"));
		root.getChildren ().remove (root.lookup ("#12Text"));
		root.getChildren ().remove (root.lookup ("#12Flags"));
		root.getChildren ().remove (root.lookup ("#12D"));
		root.getChildren ().remove (root.lookup ("#media"));
		root.getChildren ().remove (root.lookup ("#redBack"));
		root.getChildren ().remove (root.lookup ("#Top6Rect"));
		root.getChildren ().remove (root.lookup ("#Top6Text"));
		root.getChildren ().remove (root.lookup ("#superText"));
		root.getChildren ().remove (root.lookup ("#backgroundDummy"));
	}
}