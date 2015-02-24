package scoreboard;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
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

public class ConcreteOneToSevenScreenCreator extends OneToSevenScreenCreator {

	@Override
	public void showSplitScreen(final Scoreboard scoreboard,
			final Standings standings) {
		clear (scoreboard.root);
		createPointsArray (scoreboard);

		final Group to7Group = new Group ();
		to7Group.setId ("To7");
		scoreboard.root.getChildren ().add (to7Group);

		final Participant currentVoterCopy = standings.getVotes ()
				.get (scoreboard.inCountryCounter / 10).getVoter ();

		to7Group.getChildren ().add (
				RectangleBuilder
						.create ()
						.width (1920)
						.id ("background")
						.height (1080)
						.id ("redBack")
						.fill (new ImagePattern (
								scoreboard.utilities.backgroundBlue)).build ());

		scoreboard.voteSideBarCreator.makeSideOfScoreboard (to7Group,
				currentVoterCopy, scoreboard);

		final ArrayList<Rectangle> rects = new ArrayList<> ();
		final ArrayList<ImageView> flags = new ArrayList<> ();
		final ArrayList<Text> recTexts = new ArrayList<> ();

		Entry recEntry = currentVoterCopy.getEntry ();
		Media entry = recEntry.getMedia ();
		MediaPlayer entryPlayer = new MediaPlayer (entry);
		entryPlayer.setStartTime (Duration.seconds (recEntry
				.getStartDuration ()));
		entryPlayer
				.setStopTime (Duration.seconds (recEntry.getStopDuration () - 12));
		entryPlayer.setAutoPlay (true);
		entryPlayer.setVolume (0.5);
		entryPlayer.setCycleCount (1);

		MediaView entryView = MediaViewBuilder.create ()
				.mediaPlayer (entryPlayer).x (100).y (100).id ("media")
				.fitHeight (2000).fitWidth (1200).build ();

		scoreboard.root.getChildren ().add (entryView);
		entryPlayer.play ();

		entryPlayer.setOnPlaying (new Runnable () {
			@Override
			public void run() {
				for (int i = 0; i < 7; i++) {
					Rectangle rect = RectangleBuilder
							.create ()
							.height (60)
							.width (590)
							.x (690 - 590 * ((i + 1) / 4))
							.y (980 - 60 * ((i + 1) % 4))
							.fill (new ImagePattern (
									scoreboard.utilities.nationTileBackground))
							.build ();
					rects.add (rect);

					ImageView viewFlag = ImageViewBuilder
							.create ()
							.fitHeight (45)
							.fitWidth (73)
							.image (currentVoterCopy.getVotes ()
									.getReceivers ()[i].getFlag ())
							.x (rect.getX () + 105).y (rect.getY () + 7)
							.build ();
					flags.add (viewFlag);

					Text recText = TextBuilder
							.create ()
							.x (rect.getX () + 196)
							.y (rect.getY () + 40)
							.text (currentVoterCopy.getVotes ().getReceivers ()[i]
									.getName ().get ())
							.font (Font.font ("Coolvetica RG",
									FontWeight.MEDIUM, 33)).fill (Color.WHITE)
							.build ();
					recTexts.add (recText);
				}

				final ArrayList<Timeline> timelines = new ArrayList<> ();
				final ArrayList<ImageView> pointViews = new ArrayList<> ();
				for (int i = 0; i < 7; i++) {
					ImageView pointView = ImageViewBuilder
							.create ()
							.fitHeight (45)
							.fitWidth (73)
							.image (scoreboard.utilities.getPointsTokens ()
									.get (i)).x (300 + i * 73).y (850).build ();

					pointViews.add (pointView);
					double shiftPVX = rects.get (i).getX () - pointView.getX ()
							+ 15;
					double shiftPVY = rects.get (i).getY () - pointView.getY ()
							+ 9;
					to7Group.getChildren ().add (pointView);
					Timeline timeline = new Timeline ();
					timeline.getKeyFrames ().addAll (
							new KeyFrame (Duration.seconds (1), new KeyValue (
									pointView.translateXProperty (), shiftPVX),
									new KeyValue (pointView
											.translateYProperty (), shiftPVY)));
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
				final int save = scoreboard.inCountryCounter += 6;
				Platform.runLater (new VoteAdder (standings, scoreboard,
						scoreboard.utilities, save));
			}
		});

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
