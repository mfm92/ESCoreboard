package scoreboard;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaViewBuilder;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.util.Duration;
import nations.Entry;
import nations.Participant;
import data.Standings;

public class ConcreteQuickStepCreator extends IntermediatePreparator {

	@Override
	public void showSplitScreen(final Scoreboard scoreboard,
			final Standings standings, final boolean tradVP) {
		clear (scoreboard.root);
		createPointsArray (scoreboard);

		final Group to7Group = new Group ();
		to7Group.setId ("To7");
		
		if (!tradVP) {
			scoreboard.root.getChildren ().add (to7Group);	
		}

		final Participant currentVoterCopy = standings.getVotes ()
				.get (scoreboard.inCountryCounter / 10).getVoter ();

		to7Group.getChildren ().add (
				RectangleBuilder
					.create ().width (1920).id ("background").height (1080).id ("redBack")
					.fill (new ImagePattern (scoreboard.utilities.backgroundBlue)).build ());

		int entryX = 100;
		int entryY = 100;
		
		scoreboard.voteSideBarCreator.makeSideOfScoreboard (to7Group,
				currentVoterCopy, scoreboard, 900, entryX);

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

			MediaView entryView = MediaViewBuilder.create ()
					.mediaPlayer (entryPlayer).x (entryX).y (entryY).id ("media")
					.fitHeight (2000).fitWidth (1200).build ();

			scoreboard.root.getChildren ().add (entryView);
			entryPlayer.play ();

			entryPlayer.setOnPlaying (new Runnable () {
				@Override
				public void run() {

					int transSNum = scoreboard.transParts / scoreboard.columnsNrTransition + 1;
					int transSDen = (int) ((double) scoreboard.heightTransition / (double) transSNum);
					
					for (int i = 0; i < scoreboard.transParts; i++) {
						Rectangle rect = RectangleBuilder
								.create ()
								.height (transSDen)
								.width (scoreboard.columnWidthTransition)
								.x (scoreboard.transitionXOffset - scoreboard.columnWidthTransition * ((i+1) / transSNum))
								.y (scoreboard.transitionYOffset - transSDen * ((i+1) % transSNum))
								.fill (new ImagePattern (scoreboard.utilities.nationTileBackground))
								.build ();
						rects.add (rect);

						ImageView viewFlag = ImageViewBuilder
								.create ()
								.fitHeight (scoreboard.flagHeightTransition)
								.fitWidth (scoreboard.flagWidthTransition)
								.image (currentVoterCopy.getVotes ().getReceivers ()[i].getFlag ())
								.x (rect.getX () + scoreboard.pointTokenWidthTransition + scoreboard.flagFromPTOffsetTrans +
										scoreboard.ptfromEdgeOffsetTrans)
								.y (rect.getY () + (rect.getHeight () - scoreboard.flagHeightTransition) / 2)
								.build ();
						
						flags.add (viewFlag);
						
						VBox rectVBox = VBoxBuilder.create ()
								.layoutX (viewFlag.getX () + viewFlag.getFitWidth () + scoreboard.textFromFlagOffsetTrans)
								.layoutY (rect.getY ())
								.prefHeight (transSDen)
								.prefWidth (rect.getWidth () - (scoreboard.pointTokenWidthTransition + 
										scoreboard.ptfromEdgeOffsetTrans +
										scoreboard.flagFromPTOffsetTrans +
										scoreboard.flagWidthTransition +
										scoreboard.textFromFlagOffsetTrans))
								.alignment (Pos.CENTER_LEFT)
								.build ();

						Text recText = TextBuilder
								.create ()
								.text (currentVoterCopy.getVotes ().getReceivers ()[i].getName ().get ())
								.font (Font.font ("Coolvetica RG", FontWeight.MEDIUM, 33)).fill (Color.WHITE)
								.build ();
						
						rectVBox.getChildren().add (recText);
						recTexts.add (rectVBox);
					}

					final ArrayList<Timeline> timelines = new ArrayList<> ();
					final ArrayList<ImageView> pointViews = new ArrayList<> ();
					
					for (int i = 0; i < scoreboard.transParts; i++) {
						ImageView pointView = ImageViewBuilder
								.create ()
								.fitHeight (scoreboard.pointTokenHeightTransition)
								.fitWidth (scoreboard.pointTokenWidthTransition)
								.image (scoreboard.utilities.getPointsTokens ().get (i))
								.x (scoreboard.pointTokenXOffsetTransition + i *
										scoreboard.pointTokenWidthTransition)
								.y (scoreboard.pointTokenYOffsetTransition).build ();

						pointViews.add (pointView);
						double shiftPVX = rects.get (i).getX () - pointView.getX () + scoreboard.ptfromEdgeOffsetTrans;
						double shiftPVY = rects.get (i).getY () - pointView.getY () + (transSDen - scoreboard.pointTokenHeightTransition) / 2;
						
						to7Group.getChildren ().add (pointView);
						Timeline timeline = new Timeline ();
						timeline.getKeyFrames ().addAll (
								new KeyFrame (Duration.seconds (1), new KeyValue (
										pointView.translateXProperty (), shiftPVX),
										new KeyValue (pointView.translateYProperty (), shiftPVY)));
						timelines.add (timeline);
					}

					int counter = 0;
					for (final Timeline timeline : timelines) {
						timeline.setDelay (Duration.seconds (counter));
						timeline.play ();
						final int cSave = counter++;
						timeline.setOnFinished (new EventHandler<ActionEvent> () {
							@Override
							public void handle(ActionEvent event) {
								to7Group.getChildren ().add (1, rects.get (cSave));
								to7Group.getChildren ().add (flags.get (cSave));
								to7Group.getChildren ().add (recTexts.get (cSave));
								pointViews.get (cSave).toFront ();
							}
						});
					}
				}
			});

			entryPlayer.setOnEndOfMedia (new Runnable () {
				@Override
				public void run() {
					final int save = scoreboard.inCountryCounter += (scoreboard.transParts - 1);
					Platform.runLater (new VoteAdder (standings, scoreboard,
							scoreboard.utilities, save, tradVP));
				}
			});	
		} else {
			final int save = scoreboard.inCountryCounter;
			Platform.runLater (new VoteAdder (standings, scoreboard,
					scoreboard.utilities, save, tradVP));
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