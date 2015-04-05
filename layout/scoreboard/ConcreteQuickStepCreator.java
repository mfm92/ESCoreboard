package scoreboard;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
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
			final Standings standings, final boolean traditionalVoting) {
		clear (scoreboard.getRoot());
		createPointsArray (scoreboard);

		final Group to7Group = new Group ();
		to7Group.setId ("To7");
		
		if (!traditionalVoting) {
			scoreboard.getRoot().getChildren ().add (to7Group);	
		}

		final Participant currentVoterCopy = standings.getVotes ().get (scoreboard.inCountryCounter / 10).getVoter ();

		Rectangle iR = new Rectangle();
		iR.setWidth (scoreboard.getScreenWidth ());
		iR.setHeight (scoreboard.getScreenHeight ());
		iR.setId ("background");
		iR.setId ("redBack");
		
		Rectangle backgroundIntermediate = new Rectangle (scoreboard.getBackgroundWidth (), scoreboard.getBackgroundHeight ());
		backgroundIntermediate.setLayoutX ((scoreboard.getScreenWidth () - scoreboard.getBackgroundWidth ()) / 2);
		backgroundIntermediate.setLayoutY ((scoreboard.getScreenHeight () - scoreboard.getBackgroundHeight ()) / 2);
		backgroundIntermediate.setFill (new ImagePattern (scoreboard.getDataCarrier ().intermediateBackground));
		backgroundIntermediate.setId ("backgroundI");
		
		Rectangle voteBoxRectangle = new Rectangle (scoreboard.getQuickVoteBoxWidth (), scoreboard.getQuickVoteBoxHeight ());
		voteBoxRectangle.setLayoutX (scoreboard.getQuickVoteBoxX ());
		voteBoxRectangle.setLayoutY (scoreboard.getQuickVoteBoxY ());
		voteBoxRectangle.setFill (new ImagePattern (scoreboard.getDataCarrier ().voteQuickUnderlay));
		voteBoxRectangle.setId ("voteBox");
		
		Rectangle ptRect = new Rectangle();
		
		ptRect.setX (scoreboard.getPtUnderLayX ());
		ptRect.setY (scoreboard.getPtUnderLayY ());
		ptRect.setId ("ptHolder");
		ptRect.setWidth (scoreboard.getPtUnderLayWidth());
		ptRect.setHeight (scoreboard.getPtUnderLayHeight());
		ptRect.setFill (new ImagePattern (scoreboard.getDataCarrier().ptHolder));
		
		iR.setFill (new ImagePattern (scoreboard.getDataCarrier().backgroundBlue));
		to7Group.getChildren ().add(new Group (iR, backgroundIntermediate, voteBoxRectangle, ptRect));
		
		scoreboard.getRightSideBar().makeSideOfScoreboard (to7Group, currentVoterCopy, scoreboard);
		
		if (!traditionalVoting) {
			ArrayList<Rectangle> rects = new ArrayList<> ();
			ArrayList<ImageView> flags = new ArrayList<> ();
			ArrayList<VBox> recTexts = new ArrayList<> ();

			int shortage = (int)(Math.min (5.5d, 20 * (scoreboard.getVoteTokenDuration ().toSeconds () / 4d)));
			
			Entry recEntry = currentVoterCopy.getEntry ();
			Media entry = recEntry.getMedia ();
			MediaPlayer entryPlayer = new MediaPlayer (entry);
			entryPlayer.setStartTime (Duration.seconds (recEntry.getStartDuration ()));
			entryPlayer.setStopTime (Duration.seconds (recEntry.getStopDuration () - shortage));
			entryPlayer.setAutoPlay (true);
			entryPlayer.setVolume (0);
			entryPlayer.setCycleCount (1);

			MediaView entryView = new MediaView ();
			entryView.setStyle ("-fx-border-color: #000033;\n-fx-border-width: 2 2 2 2;");
			entryView.setMediaPlayer (entryPlayer);
			entryView.setPreserveRatio (false);
			entryView.setX (scoreboard.getQuickEntryX ());
			entryView.setY (scoreboard.getQuickEntryY ());
			entryView.setId ("media");
			entryView.setFitHeight (scoreboard.getQuickEntryHeight ());
			entryView.setFitWidth (scoreboard.getQuickEntryWidth ());
			
			scoreboard.getRoot().getChildren ().add (entryView);
			
			Platform.runLater(() -> entryPlayer.play ());

			entryPlayer.setOnPlaying (() -> {
				int transSNum = scoreboard.getTransParts() / scoreboard.getColumnsNrTransition() + 1;
				int transSDen = (int) ((double) scoreboard.getHeightTransition() / (double) transSNum);
				
				for (int i = 0; i < scoreboard.getTransParts (); i++) {
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
					recText.setFont (Font.font (scoreboard.getDataCarrier().font_1, FontWeight.MEDIUM, 24 * (scoreboard.getScreenWidth () / 1920d)));
					recText.setFill (Color.web ("#1C1C1C"));
					
					rectVBox.getChildren().add (recText);
					recTexts.add (rectVBox);
				}

				ArrayList<ParallelTransition> transitions = new ArrayList<>();
				ArrayList<ImageView> pointViews = new ArrayList<> ();
				
				for (int i = 0; i < 10; i++) {
					ImageView pointView = new ImageView();
					pointView.setFitHeight (scoreboard.getPointTokenHeightTransition ());
					pointView.setFitWidth (scoreboard.getPointTokenWidthTransition ());
					pointView.setImage (scoreboard.getDataCarrier ().getPointsTokens ().get (i));
					pointView.setX(scoreboard.getPointTokenXOffsetTransition() + i *+
							scoreboard.getPointTokenWidthTransition());
					pointView.setY (scoreboard.getPointTokenYOffsetTransition());

					pointViews.add (pointView);
					to7Group.getChildren ().add (pointView);
					
					if (i >= scoreboard.getTransParts()) continue;
					
					double shiftPVX = rects.get (i).getX () - pointView.getX () + scoreboard.getPtfromEdgeOffsetTrans();
					double shiftPVY = rects.get (i).getY () - pointView.getY () + (transSDen - scoreboard.getPointTokenHeightTransition()) / 2;
					
					double scaleX = ((double) scoreboard.getFlagWidthTransition () - (double) scoreboard.getPointTokenWidthTransition ()) / (double) scoreboard.getPointTokenWidthTransition ();
					double scaleY = ((double) scoreboard.getFlagHeightTransition () - (double) scoreboard.getPointTokenHeightTransition ()) / (double) scoreboard.getPointTokenHeightTransition ();
				
					TranslateTransition locShift = new TranslateTransition (scoreboard.getVoteTokenDuration ().divide (2));
					locShift.setByX (shiftPVX);
					locShift.setByY (shiftPVY);
					locShift.setNode (pointView);
					
					ScaleTransition sizeShift = new ScaleTransition (scoreboard.getVoteTokenDuration ().divide (2));
					sizeShift.setByX (scaleX);
					sizeShift.setByY (scaleY);
					sizeShift.setNode (pointView);
					
					transitions.add (new ParallelTransition (locShift, sizeShift));
				}

				int counter = 0;
				for (ParallelTransition pTrans : transitions) {
					pTrans.setDelay (Duration.seconds (1));
					pTrans.play ();
					
					final int cSave = counter++;
					pTrans.setOnFinished (event -> {
						to7Group.getChildren ().add (1, rects.get (cSave));
						to7Group.getChildren ().add (flags.get (cSave));
						to7Group.getChildren ().add (recTexts.get (cSave));
						pointViews.get (cSave).toFront ();
						
						Rectangle shiny = new Rectangle (rects.get (cSave).getHeight (), rects.get (cSave).getHeight ());
						shiny.setFill (Color.WHITE);
						shiny.setLayoutX (rects.get (cSave).getX ());
						shiny.setLayoutY (rects.get (cSave).getY ());
						shiny.setEffect (new javafx.scene.effect.MotionBlur (20, 20));
						shiny.setOpacity (0.7);
						
						to7Group.getChildren ().add (shiny);
						
						Timeline run = new Timeline();
						
						KeyFrame start = new KeyFrame (Duration.ZERO, new KeyValue (shiny.layoutXProperty (), shiny.getLayoutX ()));
						KeyFrame go = new KeyFrame (Duration.seconds (0.5 * scoreboard.getVoteTokenDuration ().toSeconds () / 2d), new KeyValue (shiny.layoutXProperty(), 
								shiny.getLayoutX () + scoreboard.getColumnWidthTransition () - shiny.getWidth ()));
						KeyFrame stay = new KeyFrame (Duration.seconds(0.5 * scoreboard.getVoteTokenDuration ().toSeconds () / 2d), new KeyValue (shiny.opacityProperty (), shiny.getOpacity ()));
						KeyFrame nowGo = new KeyFrame (Duration.seconds (0.55 * scoreboard.getVoteTokenDuration ().toSeconds () / 2d), new KeyValue (shiny.opacityProperty (), 0));
						
						run.getKeyFrames ().addAll (start, go, stay, nowGo);
						run.setDelay (Duration.seconds (0.04 * cSave));
						run.play ();
					});
				}
			});
			
			entryPlayer.setOnEndOfMedia (() -> {
				scoreboard.inCountryCounter += (scoreboard.getTransParts() - 1);
				Platform.runLater (new VoteAdder (standings, scoreboard,
						scoreboard.getDataCarrier(), scoreboard.inCountryCounter, traditionalVoting));
			});
			
		} else {
			Platform.runLater (new VoteAdder (standings, scoreboard,
					scoreboard.getDataCarrier(), scoreboard.inCountryCounter, traditionalVoting));
		}
	}

	private void clear (Group root) {
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
		root.getChildren ().remove (root.lookup ("#backgroundI"));
		root.getChildren ().remove (root.lookup ("#voteBox"));
		root.getChildren ().remove (root.lookup ("#ptHolder"));
	}
}