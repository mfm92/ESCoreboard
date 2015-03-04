package scoreboard;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;

import nations.Entry;
import nations.Participant;
import utilities.DataCarrier;
import controller.CoreUI;
import data.Standings;

public class Scoreboard extends Application {

	private boolean traditionalVoting = CoreUI.inputData.getTraditionalVoting ();
	private boolean useSpecialFlags = CoreUI.inputData.getUsePrettyFlags ();
	private boolean useFullScreen = CoreUI.inputData.getUseFullScreen ();
	private boolean createBanners = CoreUI.inputData.getBannerCreatorActivated ();
	
	private Duration minDuration = Duration.seconds (0.2);
	private Duration maxDuration = Duration.seconds (3.0);
	
	private String title = CoreUI.inputData.getNameOfEdition () + " " + CoreUI.inputData.getEditionNr () + " results";
	private Duration voteTokenDuration = Duration.seconds 
			(minDuration.toSeconds () + (1 - (CoreUI.inputData.getShowSpeed () / 100.0)) * 
					(maxDuration.toSeconds () - minDuration.toSeconds ()));

	private int columnsNr = 3;
	private int columnsNrTransition = 2;
	
	private int specialBorder = 10;
	private int transParts = 7;
	
	private int globalXOffset = 50;
	private int globalYOffset = 50;
	
	private int transitionXOffset = 690;
	private int transitionYOffset = 980;
	
	private int height = 900;
	private int heightTransition = 240;
	
	private int columnNameWidth = 500;
	private int columnWidth = 600;
	private int columnWidthTransition = 590;
	
	private int flagHeight = 53;
	private int flagWidth = (int) (1.5 * getFlagHeight());
	
	private int flagHeightTransition = 50;
	private int flagWidthTransition = (int) (1.5 * getFlagHeightTransition());
	
	private int pointTokenXOffset = 100;
	private int pointTokenYOffset = 1000;
	
	private int pointTokenXOffsetTransition = 100;
	private int pointTokenYOffsetTransition = 1000;
	
	private int ptfromEdgeOffsetTrans = 20;
	private int flagFromPTOffsetTrans = 20;
	private int textFromFlagOffsetTrans = 20;
	
	private int pointTokenWidthTransition = 75;
	private int pointTokenHeightTransition = 50;
	
	private int rightBarWidth = 520;
	private int rightBarX = 1350;
	
	private int bottomScoreboardOffset = 40;
	
	private int bottomBarHeight = 75;
	private int bottomBarWidth = 700;
	private int bottomBarX = getGlobalXOffset() + getColumnsNr()*getColumnWidth() - getBottomBarWidth();
	private int bottomBarY = getGlobalYOffset() + getHeight() + getBottomScoreboardOffset();
	
	private int ptUnderLayHeight = 75;
	private int ptUnderLayWidth = 10 * getFlagWidth() + 100;
	
	private int nameFromFlagOffset = 25;

	private SideOverviewTableCreator sideTableCreator = new SimpleSideTableStyle ();
	private TwelvePairShower twelvePairShower = new ConcreteTwelvePairShower ();
	private VoteSideBarCreator rightSideBar = new RightSideVoteBarCreator ();
	private VoteSideBarCreator bottomSideBar = new BottomSideVoteBarCreator ();
	private TileUpdater tileUpdater = new ConcreteTileUpdater ();
	private IntermediatePreparator to7ScreenMaker = new ConcreteQuickStepCreator ();

	private UpdateAnimator oneToSevenAnimator = new QuickUpdater ();
	private UpdateAnimator topThreeAnimator = new StepByStepAnimator ();

	private Group root;
	private Group superNations;
	private HashMap<Participant, Group> groupNationMap = new HashMap<> ();
	private ArrayList<Participant> participants;
	private Participant currentVoter;

	private ArrayList<Rectangle> pointViews;

	private DataCarrier dataCarrier;
	private Rectangle background;
	
	int inCountryCounter;
	
	public static void main(String[] args) {
		launch (args);
	}

	@Override
	public void start(Stage primaryStage) {
		Standings standings = null;
		try {
			setDataCarrier (new DataCarrier (isUsingSpecialFlags()));
			standings = new Standings (getDataCarrier());
		} catch (IOException e) {
			e.printStackTrace ();
		}

		inCountryCounter = 1;
		
		Rectangle background = new Rectangle ();
		background.setWidth (1920);
		background.setHeight (1080);
		background.setId ("background");
		background.setFill (new ImagePattern (getDataCarrier().backgroundWhite));

		this.setBackground (background);
		String[] finalists = getDataCarrier().getListOfNames ();
		ArrayList<Participant> rosterNations = getDataCarrier()
				.getListOfNations (finalists);
		this.setParticipants (new ArrayList<> (rosterNations));
		Collections.sort (rosterNations);

		setRoot (new Group ());
		getRoot().setId ("RootGroup");

		drawScoreboard (rosterNations, standings, primaryStage);

		Platform.runLater (showOneToSeven (standings, this));
	}

	private void drawScoreboard(
			ArrayList<Participant> rosterNations, Standings standings,
			Stage primaryStage) {
		
		Rectangle backgroundDummy = new Rectangle ();
		backgroundDummy.setWidth (1920);
		backgroundDummy.setHeight (1080);
		backgroundDummy.setId ("backgroundDummy");
		backgroundDummy.setFill (new ImagePattern (getDataCarrier().backgroundWhite));

		getTileUpdater().updateTiles (this, null);
		getRoot().getChildren ().add (getBackground());
		getRoot().getChildren ().add (getSuperNations());
		getRoot().getChildren ().add (backgroundDummy);

		Scene scene = new Scene (getRoot());

		// MAKE IT FULL SCREEN
		primaryStage.setX (Screen.getPrimary ().getVisualBounds ().getMinX ());
		primaryStage.setY (Screen.getPrimary ().getVisualBounds ().getMinY ());
		primaryStage.setWidth (Screen.getPrimary ().getVisualBounds ()
				.getWidth ());
		primaryStage.setHeight (Screen.getPrimary ().getVisualBounds ()
				.getHeight ());
		primaryStage.setScene (scene);
		primaryStage.setTitle (getTitle());
		primaryStage.setFullScreen (isUsingFullScreen());
		primaryStage.show ();
	}

	void update(final ArrayList<Participant> standings,
			final Participant voter, final ArrayList<Participant> oldStandings,
			Standings overview) {
		
		if (!isTraditionalVoting()) {
			((inCountryCounter % 10 == getTransParts()) ? getOneToSevenAnimator() : getTopThreeAnimator())
			.updateAnimate (this, voter, overview, oldStandings, standings, isTraditionalVoting());	
		} else {
			getTopThreeAnimator().updateAnimate (this, voter, overview, oldStandings, standings, isTraditionalVoting());
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
				generateImageScoreboard (getRoot(), voter);

				// SHOW 12 PAIR
				getTwelvePairShower().addTwelvePair (scoreboard, voter, receiver);

				// CREATE TABLE
				getRoot().getChildren ().add (getSideTableCreator().createSideTable (getParticipants()));

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

		MediaView entryView = new MediaView ();
		entryView.setMediaPlayer (entryPlayer);
		entryView.setFitHeight (2000);
		entryView.setFitWidth (1200);
		entryView.setX (100);
		entryView.setY (50);
		entryView.setId ("media");

		getRoot().getChildren ().add (entryView);
		entryPlayer.play ();
		entryPlayer.setOnEndOfMedia (new Runnable () {
			@Override
			public void run() {
				getRoot().getChildren ().add (0, getBackground());
				Platform.runLater (showOneToSeven (standings, scoreboard));
			}
		});
	}

	private Thread showOneToSeven(final Standings standings,
			final Scoreboard scoreboard) {
		return new Thread (new Runnable () {
			@Override
			public void run() {
				getTo7ScreenMaker().showSplitScreen (scoreboard, standings, isTraditionalVoting());
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
			ioex.printStackTrace ();
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
		if (pts == 10)
			return 9;
		if (pts == 12)
			return 0;

		return pts;
	}

	public boolean isTraditionalVoting() {
		return traditionalVoting;
	}

	public void setTraditionalVoting(boolean tradVP) {
		this.traditionalVoting = tradVP;
	}

	public boolean isCreatingBanners() {
		return createBanners;
	}

	public void setCreateBanners(boolean createBanners) {
		this.createBanners = createBanners;
	}

	public boolean isUsingSpecialFlags() {
		return useSpecialFlags;
	}

	public void setUseSpecialFlags(boolean useSpecialFlags) {
		this.useSpecialFlags = useSpecialFlags;
	}

	public boolean isUsingFullScreen() {
		return useFullScreen;
	}

	public void setUseFullScreen(boolean useFullScreen) {
		this.useFullScreen = useFullScreen;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Duration getVoteTokenDuration() {
		return voteTokenDuration;
	}

	public void setVoteTokenDuration(Duration voteTokenDuration) {
		this.voteTokenDuration = voteTokenDuration;
	}

	int getColumnsNr() {
		return columnsNr;
	}

	void setColumnsNr(int columnsNr) {
		this.columnsNr = columnsNr;
	}

	public int getColumnsNrTransition() {
		return columnsNrTransition;
	}

	public void setColumnsNrTransition(int columnsNrTransition) {
		this.columnsNrTransition = columnsNrTransition;
	}

	public int getSpecialBorder() {
		return specialBorder;
	}

	public void setSpecialBorder(int specialBorder) {
		this.specialBorder = specialBorder;
	}

	public int getTransParts() {
		return transParts;
	}

	public void setTransParts(int transParts) {
		this.transParts = transParts;
	}

	public int getGlobalXOffset() {
		return globalXOffset;
	}

	public void setGlobalXOffset(int globalXOffset) {
		this.globalXOffset = globalXOffset;
	}

	public int getGlobalYOffset() {
		return globalYOffset;
	}

	public void setGlobalYOffset(int globalYOffset) {
		this.globalYOffset = globalYOffset;
	}

	public int getTransitionXOffset() {
		return transitionXOffset;
	}

	public void setTransitionXOffset(int transitionXOffset) {
		this.transitionXOffset = transitionXOffset;
	}

	public int getTransitionYOffset() {
		return transitionYOffset;
	}

	public void setTransitionYOffset(int transitionYOffset) {
		this.transitionYOffset = transitionYOffset;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeightTransition() {
		return heightTransition;
	}

	public void setHeightTransition(int heightTransition) {
		this.heightTransition = heightTransition;
	}

	public int getColumnNameWidth() {
		return columnNameWidth;
	}

	public void setColumnNameWidth(int columnNameWidth) {
		this.columnNameWidth = columnNameWidth;
	}

	public int getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}

	public int getColumnWidthTransition() {
		return columnWidthTransition;
	}

	public void setColumnWidthTransition(int columnWidthTransition) {
		this.columnWidthTransition = columnWidthTransition;
	}

	public int getFlagHeight() {
		return flagHeight;
	}

	public void setFlagHeight(int flagHeight) {
		this.flagHeight = flagHeight;
	}

	public int getFlagWidth() {
		return flagWidth;
	}

	public void setFlagWidth(int flagWidth) {
		this.flagWidth = flagWidth;
	}

	public int getFlagHeightTransition() {
		return flagHeightTransition;
	}

	public void setFlagHeightTransition(int flagHeightTransition) {
		this.flagHeightTransition = flagHeightTransition;
	}

	public int getFlagWidthTransition() {
		return flagWidthTransition;
	}

	public void setFlagWidthTransition(int flagWidthTransition) {
		this.flagWidthTransition = flagWidthTransition;
	}

	public int getPointTokenXOffset() {
		return pointTokenXOffset;
	}

	public void setPointTokenXOffset(int pointTokenXOffset) {
		this.pointTokenXOffset = pointTokenXOffset;
	}

	public int getPointTokenYOffset() {
		return pointTokenYOffset;
	}

	public void setPointTokenYOffset(int pointTokenYOffset) {
		this.pointTokenYOffset = pointTokenYOffset;
	}

	public int getPointTokenXOffsetTransition() {
		return pointTokenXOffsetTransition;
	}

	public void setPointTokenXOffsetTransition(int pointTokenXOffsetTransition) {
		this.pointTokenXOffsetTransition = pointTokenXOffsetTransition;
	}

	public int getPointTokenYOffsetTransition() {
		return pointTokenYOffsetTransition;
	}

	public void setPointTokenYOffsetTransition(int pointTokenYOffsetTransition) {
		this.pointTokenYOffsetTransition = pointTokenYOffsetTransition;
	}

	public int getPtfromEdgeOffsetTrans() {
		return ptfromEdgeOffsetTrans;
	}

	public void setPtfromEdgeOffsetTrans(int ptfromEdgeOffsetTrans) {
		this.ptfromEdgeOffsetTrans = ptfromEdgeOffsetTrans;
	}

	public int getFlagFromPTOffsetTrans() {
		return flagFromPTOffsetTrans;
	}

	public void setFlagFromPTOffsetTrans(int flagFromPTOffsetTrans) {
		this.flagFromPTOffsetTrans = flagFromPTOffsetTrans;
	}

	public int getTextFromFlagOffsetTrans() {
		return textFromFlagOffsetTrans;
	}

	public void setTextFromFlagOffsetTrans(int textFromFlagOffsetTrans) {
		this.textFromFlagOffsetTrans = textFromFlagOffsetTrans;
	}

	public int getPointTokenWidthTransition() {
		return pointTokenWidthTransition;
	}

	public void setPointTokenWidthTransition(int pointTokenWidthTransition) {
		this.pointTokenWidthTransition = pointTokenWidthTransition;
	}

	public int getPointTokenHeightTransition() {
		return pointTokenHeightTransition;
	}

	public void setPointTokenHeightTransition(int pointTokenHeightTransition) {
		this.pointTokenHeightTransition = pointTokenHeightTransition;
	}

	public int getRightBarWidth() {
		return rightBarWidth;
	}

	public void setRightBarWidth(int rightBarWidth) {
		this.rightBarWidth = rightBarWidth;
	}

	public int getRightBarX() {
		return rightBarX;
	}

	public void setRightBarX(int rightBarX) {
		this.rightBarX = rightBarX;
	}

	public int getBottomScoreboardOffset() {
		return bottomScoreboardOffset;
	}

	public void setBottomScoreboardOffset(int bottomScoreboardOffset) {
		this.bottomScoreboardOffset = bottomScoreboardOffset;
	}

	public int getBottomBarHeight() {
		return bottomBarHeight;
	}

	public void setBottomBarHeight(int bottomBarHeight) {
		this.bottomBarHeight = bottomBarHeight;
	}

	public int getBottomBarWidth() {
		return bottomBarWidth;
	}

	public void setBottomBarWidth(int bottomBarWidth) {
		this.bottomBarWidth = bottomBarWidth;
	}

	public int getBottomBarX() {
		return bottomBarX;
	}

	public void setBottomBarX(int bottomBarX) {
		this.bottomBarX = bottomBarX;
	}

	public int getBottomBarY() {
		return bottomBarY;
	}

	public void setBottomBarY(int bottomBarY) {
		this.bottomBarY = bottomBarY;
	}

	public int getPtUnderLayHeight() {
		return ptUnderLayHeight;
	}

	public void setPtUnderLayHeight(int ptUnderLayHeight) {
		this.ptUnderLayHeight = ptUnderLayHeight;
	}

	public int getPtUnderLayWidth() {
		return ptUnderLayWidth;
	}

	public void setPtUnderLayWidth(int ptUnderLayWidth) {
		this.ptUnderLayWidth = ptUnderLayWidth;
	}

	public int getNameFromFlagOffset() {
		return nameFromFlagOffset;
	}

	public void setNameFromFlagOffset(int nameFromFlagOffset) {
		this.nameFromFlagOffset = nameFromFlagOffset;
	}

	public SideOverviewTableCreator getSideTableCreator() {
		return sideTableCreator;
	}

	public void setSideTableCreator(SideOverviewTableCreator sideTableCreator) {
		this.sideTableCreator = sideTableCreator;
	}

	public TwelvePairShower getTwelvePairShower() {
		return twelvePairShower;
	}

	public void setTwelvePairShower(TwelvePairShower twelvePairShower) {
		this.twelvePairShower = twelvePairShower;
	}

	public VoteSideBarCreator getRightSideBar() {
		return rightSideBar;
	}

	public void setRightSideBar(VoteSideBarCreator rightSideBar) {
		this.rightSideBar = rightSideBar;
	}

	public VoteSideBarCreator getBottomSideBar() {
		return bottomSideBar;
	}

	public void setBottomSideBar(VoteSideBarCreator bottomSideBar) {
		this.bottomSideBar = bottomSideBar;
	}

	public TileUpdater getTileUpdater() {
		return tileUpdater;
	}

	public void setTileUpdater(TileUpdater tileUpdater) {
		this.tileUpdater = tileUpdater;
	}

	public IntermediatePreparator getTo7ScreenMaker() {
		return to7ScreenMaker;
	}

	public void setTo7ScreenMaker(IntermediatePreparator to7ScreenMaker) {
		this.to7ScreenMaker = to7ScreenMaker;
	}

	public UpdateAnimator getOneToSevenAnimator() {
		return oneToSevenAnimator;
	}

	public void setOneToSevenAnimator(UpdateAnimator oneToSevenAnimator) {
		this.oneToSevenAnimator = oneToSevenAnimator;
	}

	public UpdateAnimator getTopThreeAnimator() {
		return topThreeAnimator;
	}

	public void setTopThreeAnimator(UpdateAnimator topThreeAnimator) {
		this.topThreeAnimator = topThreeAnimator;
	}

	public Group getRoot() {
		return root;
	}

	public void setRoot(Group root) {
		this.root = root;
	}

	public Group getSuperNations() {
		return superNations;
	}

	public void setSuperNations(Group superNations) {
		this.superNations = superNations;
	}

	public HashMap<Participant, Group> getGroupNationMap() {
		return groupNationMap;
	}

	public void setGroupNationMap(HashMap<Participant, Group> groupNationMap) {
		this.groupNationMap = groupNationMap;
	}

	ArrayList<Participant> getParticipants() {
		return participants;
	}

	void setParticipants(ArrayList<Participant> participants) {
		this.participants = participants;
	}

	Participant getCurrentVoter() {
		return currentVoter;
	}

	void setCurrentVoter(Participant currentVoter) {
		this.currentVoter = currentVoter;
	}

	public ArrayList<Rectangle> getPointViews() {
		return pointViews;
	}

	public void setPointViews(ArrayList<Rectangle> pointViews) {
		this.pointViews = pointViews;
	}

	public DataCarrier getDataCarrier() {
		return dataCarrier;
	}

	public void setDataCarrier(DataCarrier utilities) {
		this.dataCarrier = utilities;
	}

	public Rectangle getBackground() {
		return background;
	}

	public void setBackground(Rectangle background) {
		this.background = background;
	}
}