package scoreboard;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
import utilities.Utilities;
import controller.CoreUI;
import data.Standings;

public class Scoreboard {
	
	// ----------------------------------- //
	private Group root;
	private Group superNations;
	private HashMap<Participant, Group> groupNationMap = new HashMap<> ();
	private ArrayList<Participant> participants;
	private Participant currentVoter;
	private ArrayList<Rectangle> pointViews;
	// ----------------------------------- //
	
	// ----------------------------------- //
	private boolean traditionalVoting = CoreUI.inputData.getTraditionalVoting ();
	private boolean useSpecialFlags = CoreUI.inputData.getUsePrettyFlags ();
	private boolean useFullScreen = CoreUI.inputData.getUseFullScreen ();
	private boolean createBanners = CoreUI.inputData.getBannerCreatorActivated ();
	// ----------------------------------- //
	
	// ----------------------------------- //
	private Duration minDuration = Duration.seconds (0.8);
	private Duration maxDuration = Duration.seconds (4.0);
	
	private String title = CoreUI.inputData.getNameOfEdition () + " " + CoreUI.inputData.getEditionNr () + " results";
	private Duration voteTokenDuration = Duration.seconds 
			(minDuration.toSeconds () + (1 - (CoreUI.inputData.getShowSpeed () / 100.0)) * 
					(maxDuration.toSeconds () - minDuration.toSeconds ()));
	// ----------------------------------- //
	
	// ----------------------------- //
	private int screenWidth = 1920;
	private int screenHeight = 1080;
	// ----------------------------- //
	
	// ----------------------------- //	
	private int backgroundWidth = (int) (0.85 * screenWidth);
	private int backgroundHeight = (int) (0.85 * screenHeight);
	// ----------------------------- //
	
	// ----------------------------- //
	private int columnsNr = 2;
	private int scWidth = (int) (0.65 * backgroundWidth);
	private int widthFromLeftOffset = (int) (0.03 * backgroundWidth);
	
	private int columnWidth = scWidth / columnsNr;
	private int columnNameWidth = (int) (0.8 * (double)(columnWidth));
	// ----------------------------- //
	
	// ----------------------------- //
	private int specialBorder = 10;
	private int transParts = 7;
	// ----------------------------- //
	
	// ----------------------------- //
	private int height = (int) (0.8 * backgroundHeight);
	private int heightFromTopOffset = (int) (0.05 * backgroundHeight);
	private int rowHeight = 
			(int)(((double) height / 
				Math.ceil((double) 
						CoreUI.inputData.getParticipants ().stream ().filter (t -> t.getStatus ().equals ("P")).count () / 
						(double) columnsNr)));
	// ----------------------------- //

	// ----------------------------- //
	private int flagHeight = (int) (0.9 * (double) rowHeight);
	private int flagWidth = (int) (1.5 * (double) flagHeight);
	
	private int nameFromFlagOffset = (int)(flagWidth * 0.3);
	// ----------------------------- //
	
	// ----------------------------- //
	private int bottomScoreboardOffset = (int) (0.01 * backgroundHeight);
	
	private int bottomBarHeight = (int)(0.07*backgroundHeight);
	private int bottomBarWidth = scWidth;
	private int bottomBarX = (screenWidth - backgroundWidth) / 2 + widthFromLeftOffset;
	private int bottomBarY = (screenHeight - backgroundHeight) / 2 + heightFromTopOffset + height + bottomScoreboardOffset;
	// ----------------------------- //
	
	// ----------------------------- //
	private int ptUnderLayHeight = (int)(0.0775*backgroundHeight);
	private int ptUnderLayWidth = scWidth;
	private int ptUnderLayX = (screenWidth - backgroundWidth) / 2 + widthFromLeftOffset;
	private int ptUnderLayY = (screenHeight - backgroundHeight) / 2 + heightFromTopOffset + height + bottomScoreboardOffset;
	
	private int pointTokenXOffset = ptUnderLayX;
	private int pointTokenHeight = (int)((9d/10d) * ptUnderLayHeight);
	private int pointTokenYOffset = ptUnderLayY + (ptUnderLayHeight - pointTokenHeight) / 2;
	private int pointTokenWidth = (int)((1d/10d) * ptUnderLayWidth);
	// ----------------------------- //
	
	// ----------------------------- //
	private int rightBarWidth = (int)(0.27 * backgroundWidth);
	private int rightBarX = (int)(0.7 * backgroundWidth) + (screenWidth - backgroundWidth) / 2;
	
	int rightBarHeight = height + bottomScoreboardOffset + ptUnderLayHeight;
	int rightBarY = heightFromTopOffset + (screenHeight - backgroundHeight) / 2;
	// ----------------------------- //
	
	// ----------------------------- //
	private int quickEntryX = (screenWidth - backgroundWidth) / 2 + widthFromLeftOffset;
	private int quickEntryWidth = scWidth;
	private int quickEntryY = (screenHeight - backgroundHeight) / 2 + heightFromTopOffset;
	private int quickEntryHeight = (int)(0.6 * backgroundHeight);
	
	private int quickVoteBoxX = quickEntryX;
	private int quickVoteBoxWidth = quickEntryWidth;
	private int quickVoteBoxY = quickEntryY + quickEntryHeight;
	private int quickVoteBoxHeight = (int)(0.2 * backgroundHeight);
	// ----------------------------- //
	
	// ----------------------------- //
	private int columnsNrTransition = columnsNr;
	private int columnWidthTransition = quickEntryWidth / columnsNrTransition;
	private int heightTransition = (int) (0.2 * backgroundHeight);
	private int transitionXOffset = (screenWidth - backgroundWidth) / 2 + widthFromLeftOffset +
			(columnsNrTransition-1) * columnWidthTransition;
	
	private int rowHeightTransition = (int)((heightTransition / (((int)(Math.ceil(transParts / columnsNrTransition))) + 1)));
	private int flagHeightTransition = (int)(0.9*rowHeightTransition);
	private int flagWidthTransition = (int)(1.5*flagHeightTransition);
	
	private int transitionYOffset = (screenHeight - backgroundHeight) / 2 + heightFromTopOffset +
			quickEntryHeight + heightTransition - rowHeightTransition;
	
	private int ptfromEdgeOffsetTrans = -8;
	private int flagFromPTOffsetTrans = -8;
	private int textFromFlagOffsetTrans = 16;
	// ----------------------------- //
	
	// ----------------------------- //
	private int pointTokenXOffsetTransition = pointTokenXOffset;
	private int pointTokenYOffsetTransition = pointTokenYOffset;
	
	private int pointTokenWidthTransition = pointTokenWidth;
	private int pointTokenHeightTransition = pointTokenHeight;
	// ----------------------------- //

	//------------//
	private SideOverviewTableCreator sideTableCreator = new SimpleSideTableStyle ();
	private TwelvePairShower twelvePairShower = new ConcreteTwelvePairShower ();
	private VoteSideBarCreator rightSideBar = new RightSideVoteBarCreator ();
	private VoteSideBarCreator bottomSideBar = new BottomSideVoteBarCreator ();
	private TileUpdater tileUpdater = new ConcreteTileUpdater ();
	private IntermediatePreparator to7ScreenMaker = new ConcreteQuickStepCreator ();

	private UpdateAnimator oneToSevenAnimator = new QuickUpdater ();
	private UpdateAnimator topThreeAnimator = new StepByStepAnimator ();
	//------------//

	// --------------- //
	private Utilities dataCarrier;
	private Rectangle background;
	private Standings standings;
	int inCountryCounter;
	// --------------- //
	
	public void start(Stage primaryStage) throws InterruptedException, IOException {		
		drawScoreboard (primaryStage);
		
		Scene scene = new Scene (root);
		
		primaryStage.setX (Screen.getPrimary ().getVisualBounds ().getMinX ());
		primaryStage.setY (Screen.getPrimary ().getVisualBounds ().getMinY ());
		primaryStage.setWidth (Screen.getPrimary ().getVisualBounds ().getWidth ());
		primaryStage.setHeight (Screen.getPrimary ().getVisualBounds ().getHeight ());
		primaryStage.setScene (scene);
		primaryStage.setTitle (title);
		primaryStage.setFullScreen (useFullScreen);
		primaryStage.show ();
		
		runCycle ();
	}
	
	private void runCycle() {
		Platform.runLater (() -> to7ScreenMaker.showSplitScreen (this, standings, traditionalVoting));
	}

	private void drawScoreboard (Stage primaryStage) throws IOException, InterruptedException {
		
		this.dataCarrier = new Utilities (useSpecialFlags);
		dataCarrier.initialize ();
		this.standings = new Standings (dataCarrier);

		inCountryCounter = 1;
		
		Rectangle background = new Rectangle ();
		background.setWidth (screenWidth);
		background.setHeight (screenHeight);
		background.setId ("background");
		background.setFill (new ImagePattern (dataCarrier.backgroundWhite));

		this.background = background;
		String[] finalists = dataCarrier.getListOfNames ();
		ArrayList<Participant> rosterNations = dataCarrier.getListOfNations (finalists);
		this.participants = new ArrayList<> (rosterNations);
		Collections.sort (rosterNations);
		
		root = new Group();
		root.setId ("RootGroup");
		
		Rectangle backgroundDummy = new Rectangle ();
		backgroundDummy.setWidth (screenWidth);
		backgroundDummy.setHeight (screenHeight);
		backgroundDummy.setId ("backgroundDummy");
		
		backgroundDummy.setFill (new ImagePattern (dataCarrier.backgroundWhite));

		tileUpdater.updateTiles (this, null);
		
		root.getChildren ().addAll (background, superNations, backgroundDummy);
	}

	void update(final ArrayList<Participant> standings,
			final Participant voter, final ArrayList<Participant> oldStandings,
			Standings overview) {
		
		((inCountryCounter % 10 == transParts && !traditionalVoting) ? oneToSevenAnimator : topThreeAnimator).updateAnimate (this, voter, overview, oldStandings, standings, traditionalVoting);	
	}

	// NEED A DEEP COPY
	Thread showAndPraise12Pointer(final Participant receiver,
			final Participant voter, final Standings standings, final int save,
			final Scoreboard scoreboard) {

		return new Thread (() -> {
			// GENERATE SCOREBOARD
			generateImageScoreboard (root, voter);

			// SHOW 12 PAIR
			twelvePairShower.addTwelvePair (scoreboard, voter, receiver);

			// CREATE TABLE
			root.getChildren ().add (sideTableCreator.createSideTable (participants));

			// GET THE VIDEO
			showVideoAndDirect (receiver, standings, scoreboard);
		});
	}

	private void showVideoAndDirect(Participant receiver,
			final Standings standings, final Scoreboard scoreboard) {
		Entry recEntry = receiver.getEntry ();
		
		Media entry = recEntry.getMedia ();
		MediaPlayer entryPlayer = new MediaPlayer (entry);
		entryPlayer.setStartTime (Duration.seconds (recEntry.getStartDuration ()));
		entryPlayer.setStopTime (Duration.seconds (recEntry.getStopDuration () - 19));
		entryPlayer.setAutoPlay (true);
		entryPlayer.setVolume (0);
		entryPlayer.setCycleCount (1);

		MediaView entryView = new MediaView ();
		entryView.setPreserveRatio (false);
		entryView.setMediaPlayer (entryPlayer);
		entryView.setFitHeight (674);
		entryView.setFitWidth (1200);
		entryView.setX (50);
		entryView.setY (50);
		entryView.setId ("media");

		root.getChildren ().add (entryView);
		entryPlayer.play ();
		entryPlayer.setOnEndOfMedia (() -> {
			root.getChildren ().add (0, getBackground());
			runCycle();
		});	
	}

	void generateImageScoreboard(Node rootNode, Participant voter) {
		BufferedImage bufferedImage = new BufferedImage (screenWidth, screenHeight,
				BufferedImage.TYPE_INT_ARGB);
		WritableImage scoreboardImage = rootNode.snapshot (
				new SnapshotParameters (), null);
		BufferedImage scoreboard = SwingFXUtils.fromFXImage (scoreboardImage,
				bufferedImage);
		Path basePath = Paths.get ("scoreboards");
		Path scPath = Paths.get ("scoreboards/" + title);
		File destScoreboard = null;
		
		if (!Files.exists (basePath)) {
			File base = basePath.toFile ();
			base.mkdirs ();
		}
		
		if (!Files.exists (scPath)) {
			File base = scPath.toFile ();
			base.mkdirs ();
		}
		
		destScoreboard = new File ("scoreboards/" + title + "/" + voter.getName () + ".png");
		destScoreboard.getParentFile ().mkdirs ();

		try {
			Graphics2D g2d = (Graphics2D) scoreboard.getGraphics ();
			g2d.translate (screenWidth, screenHeight);
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

	public int getScoreboardHeight() {
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

	public Utilities getDataCarrier() {
		return dataCarrier;
	}

	public void setDataCarrier(Utilities utilities) {
		this.dataCarrier = utilities;
	}

	public Rectangle getBackground() {
		return background;
	}

	public void setBackground(Rectangle background) {
		this.background = background;
	}

	public int getScoreboardWidth() {
		return scWidth;
	}

	public void setScoreboardWidth(int scWidth) {
		this.scWidth = scWidth;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public int getBackgroundWidth() {
		return backgroundWidth;
	}

	public void setBackgroundWidth(int backgroundWidth) {
		this.backgroundWidth = backgroundWidth;
	}

	public int getBackgroundHeight() {
		return backgroundHeight;
	}

	public void setBackgroundHeight(int backgroundHeight) {
		this.backgroundHeight = backgroundHeight;
	}

	public int getWidthFromLeftOffset() {
		return widthFromLeftOffset;
	}

	public void setWidthFromLeftOffset(int widthFromLeftOffset) {
		this.widthFromLeftOffset = widthFromLeftOffset;
	}

	public int getHeightFromTopOffset() {
		return heightFromTopOffset;
	}

	public void setHeightFromTopOffset(int heightFromTopOffset) {
		this.heightFromTopOffset = heightFromTopOffset;
	}

	public int getPtUnderLayX() {
		return ptUnderLayX;
	}

	public void setPtUnderLayX(int ptUnderLayX) {
		this.ptUnderLayX = ptUnderLayX;
	}

	public int getPtUnderLayY() {
		return ptUnderLayY;
	}

	public void setPtUnderLayY(int ptUnderLayY) {
		this.ptUnderLayY = ptUnderLayY;
	}

	public int getQuickEntryX() {
		return quickEntryX;
	}

	public void setQuickEntryX(int quickEntryX) {
		this.quickEntryX = quickEntryX;
	}

	public int getQuickEntryWidth() {
		return quickEntryWidth;
	}

	public void setQuickEntryWidth(int quickEntryWidth) {
		this.quickEntryWidth = quickEntryWidth;
	}

	public int getQuickEntryY() {
		return quickEntryY;
	}

	public void setQuickEntryY(int quickEntryY) {
		this.quickEntryY = quickEntryY;
	}

	public int getQuickEntryHeight() {
		return quickEntryHeight;
	}

	public void setQuickEntryHeight(int quickEntryHeight) {
		this.quickEntryHeight = quickEntryHeight;
	}

	public int getQuickVoteBoxWidth() {
		return quickVoteBoxWidth;
	}

	public void setQuickVoteBoxWidth(int quickVoteBoxWidth) {
		this.quickVoteBoxWidth = quickVoteBoxWidth;
	}

	public int getQuickVoteBoxX() {
		return quickVoteBoxX;
	}

	public void setQuickVoteBoxX(int quickVoteBoxX) {
		this.quickVoteBoxX = quickVoteBoxX;
	}

	public int getQuickVoteBoxY() {
		return quickVoteBoxY;
	}

	public void setQuickVoteBoxY(int quickVoteBoxY) {
		this.quickVoteBoxY = quickVoteBoxY;
	}

	public int getQuickVoteBoxHeight() {
		return quickVoteBoxHeight;
	}

	public void setQuickVoteBoxHeight(int quickVoteBoxHeight) {
		this.quickVoteBoxHeight = quickVoteBoxHeight;
	}

	public int getPointTokenHeight() {
		return pointTokenHeight;
	}

	public void setPointTokenHeight(int pointTokenHeight) {
		this.pointTokenHeight = pointTokenHeight;
	}

	public int getPointTokenWidth() {
		return pointTokenWidth;
	}

	public void setPointTokenWidth(int pointTokenWidth) {
		this.pointTokenWidth = pointTokenWidth;
	}
}