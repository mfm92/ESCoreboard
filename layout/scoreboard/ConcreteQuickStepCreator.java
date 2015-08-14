package scoreboard;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
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
		final Participant next = ((scoreboard.inCountryCounter + 10) / 10) >= standings.getVotes ().size () ? null :
			standings.getVotes ().get ((scoreboard.inCountryCounter + 10) / 10).getVoter ();
		
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
		
		scoreboard.getRightSideBar().makeSideOfScoreboard (to7Group, currentVoterCopy, next, scoreboard);
		
		if (!traditionalVoting) {
			ArrayList<Rectangle> rects = new ArrayList<> ();
			ArrayList<ImageView> flags = new ArrayList<> ();
			ArrayList<VBox> recTexts = new ArrayList<> ();

//			int shortage = (int)(Math.min (5.5d, 20 * (scoreboard.getVoteTokenDuration ().toSeconds () / 4d)));
			
			Entry recEntry = currentVoterCopy.getEntry ();
			Media entry = recEntry.getMedia ();
			MediaPlayer entryPlayer = new MediaPlayer (entry);
			entryPlayer.setStartTime (Duration.seconds (recEntry.getStartDuration ()));
			entryPlayer.setStopTime (Duration.seconds (recEntry.getStopDuration () + 5));
			entryPlayer.setAutoPlay (true);
			entryPlayer.setVolume (0.3);
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
					recText.setFill (Color.WHITE);
					
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
					
					ScaleTransition scaleTrans = new ScaleTransition (scoreboard.getVoteTokenDuration ().divide (2), pointView);
					scaleTrans.setFromX (1);
					scaleTrans.setToX (0.3);
					scaleTrans.setFromY (1);
					scaleTrans.setToY (0.3);
					
					RotateTransition rotTrans = new RotateTransition (scoreboard.getVoteTokenDuration ().divide (2), pointView);
					rotTrans.setByAngle (-180);
					
					FadeTransition fadeTrans = new FadeTransition (scoreboard.getVoteTokenDuration ().divide (2), pointView);
					fadeTrans.setFromValue (1);
					fadeTrans.setToValue (0);
					
					transitions.add (new ParallelTransition (scaleTrans, fadeTrans));
				}

				int counter = 0;
				for (ParallelTransition pTrans : transitions) {
					final int cSave = counter++;
					
					Rectangle rect = rects.get (cSave);
					ImageView flag = flags.get (cSave);
					VBox textBox = recTexts.get (cSave);
					
					rect.setY (rect.getY() - 100);
					flag.setY (flag.getY() - 100);
					textBox.setLayoutY (textBox.getLayoutY() - 100);
					
					for (Node n : Arrays.asList (rect, flag, textBox)) {
						FadeTransition fadeTrans = new FadeTransition (scoreboard.getVoteTokenDuration ().divide (2), n);
						fadeTrans.setFromValue (0);
						fadeTrans.setToValue (1);
						
						TranslateTransition tTrans = new TranslateTransition (scoreboard.getVoteTokenDuration ().divide (2), n);
						tTrans.setByY (100);
						
						ScaleTransition scTrans = new ScaleTransition (scoreboard.getVoteTokenDuration().divide(2), n);
						scTrans.setFromX (1);
						scTrans.setToX (0.3);
						scTrans.setFromY (1);
						scTrans.setToY (0.3);
						
						ParallelTransition piTrans = new ParallelTransition (fadeTrans, tTrans);
						piTrans.setDelay (Duration.seconds (0.3));
						piTrans.play ();
					}
					
					to7Group.getChildren ().add (1, rects.get (cSave));
					to7Group.getChildren ().add (flags.get (cSave));
					to7Group.getChildren ().add (recTexts.get (cSave));
					
					pTrans.setDelay (scoreboard.getVoteTokenDuration ().divide (2));
					pTrans.play ();
					
					pTrans.setOnFinished (event -> {
						for (int i = 0; i < scoreboard.getTransParts(); i++) {
							
							double scaleWidth = scoreboard.getScreenWidth () / 1920d;
							double scaleHeight = scoreboard.getScoreboardHeight () / 1080d;
							
							ImageView pointView = pointViews.get (i);
							pointView.setX (rects.get (i).getX () + scoreboard.getPtfromEdgeOffsetTrans() + 23 * scaleWidth);
							pointView.setY (rects.get (i).getY () + 15 * scaleHeight + 100 + (transSDen - scoreboard.getPointTokenHeightTransition()) / 2);
							
							pointView.setFitHeight (scoreboard.getFlagHeightTransition ());
							pointView.setFitWidth (scoreboard.getFlagWidthTransition ());
							
							ScaleTransition scaleTrans = new ScaleTransition (scoreboard.getVoteTokenDuration ().divide (2), pointView);
							scaleTrans.setFromX (0);
							scaleTrans.setToX (1);
							scaleTrans.setFromY (0);
							scaleTrans.setToY (1);
							
							FadeTransition fadeTrans = new FadeTransition (scoreboard.getVoteTokenDuration ().divide (4), pointView);
							fadeTrans.setFromValue (0);
							fadeTrans.setToValue (1);
							
							ParallelTransition pTransI = new ParallelTransition (scaleTrans, fadeTrans);
							
							if (i == scoreboard.getTransParts () - 1 && cSave == transitions.size () - 1) {
								pTransI.setOnFinished (eventI -> {
									for (int j = 0; j < scoreboard.getTransParts(); j++) {
										pointViews.get (j).toFront ();
										
										Rectangle shiny = new Rectangle (rects.get (j).getHeight (), rects.get (j).getHeight ());
										shiny.setFill (Color.WHITE);
										shiny.setLayoutX (rects.get (j).getX ());
										shiny.setLayoutY (rects.get (j).getY () + 100);
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
										run.setDelay (Duration.seconds (0.04 * j));
										run.play ();
									}
								});	
							}
							
							pTransI.play ();
						}
					});
				}
			});
			
			entryPlayer.setOnEndOfMedia (() -> {				
				clearNotSo (scoreboard.getRoot());
				entryPlayer.dispose ();
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
	
	private void clearNotSo (Group root) {
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
	}
}