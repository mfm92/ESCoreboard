package scoreboard;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaViewBuilder;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;

import nations.Entry;
import nations.Participant;
import utilities.NSCUtilities;

import com.sun.istack.internal.logging.Logger;

import data.Standings;

public class Scoreboard extends Application {

	Group root;
	Group superNations;
	HashMap<Participant, Group> groupNationMap = new HashMap<> ();
	ArrayList<Participant> participants;

	int columnsNr = 3;
	int columnsNrTransition = 2;
	
	int specialBorder = 10;
	int transParts = 7;
	
	int globalXOffset = 50;
	int globalYOffset = 50;
	
	int transitionXOffset = 690;
	int transitionYOffset = 980;
	
	int height = 900;
	int heightTransition = 240;
	
	int columnNameWidth = 500;
	int columnWidth = 600;
	int columnWidthTransition = 590;
	
	int flagHeight = 53;
	int flagWidth = (int) (1.5 * flagHeight);
	
	int flagHeightTransition = 50;
	int flagWidthTransition = (int) (1.5 * flagHeightTransition);
	
	int pointTokenXOffset = 100;
	int pointTokenYOffset = 1000;
	
	int pointTokenXOffsetTransition = 100;
	int pointTokenYOffsetTransition = 1000;
	
	int ptfromEdgeOffsetTrans = 20;
	int flagFromPTOffsetTrans = 20;
	int textFromFlagOffsetTrans = 20;
	
	int pointTokenWidthTransition = 75;
	int pointTokenHeightTransition = 50;
	
	int rightBarWidth = 520;
	int rightBarX = 1350;
	
	int bottomScoreboardOffset = 40;
	
	int bottomBarHeight = 75;
	int bottomBarWidth = 700;
	int bottomBarX = globalXOffset + columnsNr*columnWidth - bottomBarWidth;
	int bottomBarY = globalYOffset + height + bottomScoreboardOffset;
	
	int ptUnderLayHeight = 75;
	int ptUnderLayWidth = 10 * flagWidth + 100;
	
	int nameFromFlagOffset = 25;
	
	boolean tradVP = true;
	boolean useSpecialFlags = true;
	
	Participant currentVoter;

	ArrayList<Rectangle> pointViews;

	NSCUtilities utilities;
	Rectangle background;
	int inCountryCounter;

	SideOverviewTableCreator sideTableCreator = new SimpleSideTableStyle ();
	TwelvePairShower twelvePairShower = new ConcreteTwelvePairShower ();
	VoteSideBarCreator rightSideBar = new RightSideVoteBarCreator ();
	VoteSideBarCreator bottomSideBar = new BottomSideVoteBarCreator ();
	TileUpdater tileUpdater = new ConcreteTileUpdater ();
	IntermediatePreparator to7ScreenMaker = new ConcreteQuickStepCreator ();

	UpdateAnimator oneToSevenAnimator = new QuickUpdater ();
	UpdateAnimator topThreeAnimator = new StepByStepAnimator ();

	public static void main(String[] args) {
		launch (args);
	}

	@Override
	public void start(Stage primaryStage) {
		Standings standings = null;
		try {
			utilities = new NSCUtilities (useSpecialFlags);
			standings = new Standings (utilities);
		} catch (IOException e) {
			e.printStackTrace ();
		}

		inCountryCounter = 1;
		Rectangle background = RectangleBuilder.create ().width (1920)
				.id ("background").height (1080)
				.fill (new ImagePattern (utilities.backgroundWhite)).build ();

		this.background = background;
		String[] finalists = utilities.getListOfNames ();
		ArrayList<Participant> rosterNations = utilities
				.getListOfNations (finalists);
		this.participants = new ArrayList<> (rosterNations);
		Collections.sort (rosterNations);

		root = new Group ();
		root.setId ("RootGroup");

		drawScoreboard (rosterNations, standings, primaryStage);

		Platform.runLater (showOneToSeven (standings, this));
	}

	private void drawScoreboard(
			ArrayList<Participant> rosterNations, Standings standings,
			Stage primaryStage) {

		tileUpdater.updateTiles (this, null);
		root.getChildren ().add (background);
		root.getChildren ().add (superNations);
		root.getChildren ().add (
				RectangleBuilder.create ().width (1920).id ("backgroundDummy")
						.height (1080)
						.fill (new ImagePattern (utilities.backgroundWhite))
						.build ());

		Scene scene = SceneBuilder.create ().root (root).build ();

		// MAKE IT FULL SCREEN
		primaryStage.setX (Screen.getPrimary ().getVisualBounds ().getMinX ());
		primaryStage.setY (Screen.getPrimary ().getVisualBounds ().getMinY ());
		primaryStage.setWidth (Screen.getPrimary ().getVisualBounds ()
				.getWidth ());
		primaryStage.setHeight (Screen.getPrimary ().getVisualBounds ()
				.getHeight ());
		primaryStage.setScene (scene);
		primaryStage.setTitle ("NSC 121 Results");
		primaryStage.setFullScreen (true);
		primaryStage.show ();
	}

	void update(final ArrayList<Participant> standings,
			final Participant voter, final ArrayList<Participant> oldStandings,
			Standings overview) {
		
		if (!tradVP) {
			((inCountryCounter % 10 == transParts) ? oneToSevenAnimator : topThreeAnimator)
			.updateAnimate (this, voter, overview, oldStandings, standings, tradVP);	
		} else {
			topThreeAnimator.updateAnimate (this, voter, overview, oldStandings, standings, tradVP);
		}
	}

	// NEED A DEEP COPY
	Thread showAndPraise12Pointer(final Participant receiver,
			final Participant voter, final Standings standings, final int save,
			final Scoreboard scoreboard) {

		return new Thread (new Runnable () {

			@Override
			public void run() {

				// GENERATE SCOREBOARD
				generateImageScoreboard (root, voter);

				// SHOW 12 PAIR
				twelvePairShower.addTwelvePair (scoreboard, voter, receiver);

				// CREATE TABLE
				root.getChildren ().add (sideTableCreator.createSideTable (participants));

				// GET THE VIDEO
				showVideoAndDirect (receiver, standings, scoreboard);
			}
		});
	}

	private void showVideoAndDirect(Participant receiver,
			final Standings standings, final Scoreboard scoreboard) {
		Entry recEntry = receiver.getEntry ();
		Media entry = recEntry.getMedia ();
		MediaPlayer entryPlayer = new MediaPlayer (entry);
		entryPlayer.setStartTime (Duration.seconds (recEntry.getStartDuration ()));
		entryPlayer.setStopTime (Duration.seconds (recEntry.getStopDuration ()));
		entryPlayer.setAutoPlay (true);
		entryPlayer.setVolume (0.5);
		entryPlayer.setCycleCount (1);

		MediaView entryView = MediaViewBuilder.create ()
				.mediaPlayer (entryPlayer).x (100).y (50).id ("media")
				.fitHeight (2000).fitWidth (1200).build ();

		root.getChildren ().add (entryView);
		entryPlayer.play ();
		entryPlayer.setOnEndOfMedia (new Runnable () {
			@Override
			public void run() {
				root.getChildren ().add (0, background);
				Platform.runLater (showOneToSeven (standings, scoreboard));
			}
		});
	}

	private Thread showOneToSeven(final Standings standings,
			final Scoreboard scoreboard) {
		return new Thread (new Runnable () {
			@Override
			public void run() {
				to7ScreenMaker.showSplitScreen (scoreboard, standings, tradVP);
			}
		});
	}

	void generateImageScoreboard(Node rootNode, Participant voter) {
		BufferedImage bufferedImage = new BufferedImage (1920, 1080,
				BufferedImage.TYPE_INT_ARGB);
		WritableImage scoreboardImage = rootNode.snapshot (
				new SnapshotParameters (), null);
		BufferedImage scoreboard = SwingFXUtils.fromFXImage (scoreboardImage,
				bufferedImage);
		File destScoreboard = new File (System.getProperty ("user.dir") + "\\scoreboards\\"
						+ voter.getName () + ".png");
		destScoreboard.getParentFile ().mkdirs ();

		try {
			Graphics2D g2d = (Graphics2D) scoreboard.getGraphics ();
			g2d.translate (1920, 1080);
			ImageIO.write (scoreboard, "png", destScoreboard);
		} catch (IOException ioex) {
			Logger.getLogger (Scoreboard.class).log (Level.SEVERE,
					ioex.getMessage ());
		}
	}
	
	public int indicesToPoints(int index) {
		if (index == 0)
			return 12;
		if (index == 9)
			return 10;

		return index;
	}
	
	public int pointsToIndices(int pts) {
		if (pts <= 8)
			return pts;
		if (pts == 10)
			return 9;
		if (pts == 12)
			return 0;

		System.out.println ("pointsToIndices returns sth baaaad, pts was: "
				+ pts);

		return 103859; // absolutely pointless huehue
	}
}