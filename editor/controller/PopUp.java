package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import controller.CoreUI.PopUpMessage;

public class PopUp {

	PopUpMessage style;
	String message;
	CoreUI wrapper;

	public PopUp(PopUpMessage style, CoreUI wrapper, String message) {
		this.style = style;
		this.message = message;
		this.wrapper = wrapper;
	}

	public void show() {
		Group popUpRoot = new Group ();

		Rectangle background = new Rectangle (350, 120);
		background.setOpacity (0.9f);
		background.setFill (new ImagePattern (wrapper.getPopUpBackground (style)));

		VBox textBox = new VBox ();
		textBox.setPrefHeight (background.getHeight ());
		textBox.setPrefWidth (background.getWidth ());
		textBox.setAlignment (Pos.CENTER);

		Text text = new Text (message);
		text.setFill (Color.WHITE);
		text.setFont (Font.font ("Roboto Lt", FontWeight.BOLD, message.length () > 50 ? 18 : 24));
		textBox.getChildren ().add (text);

		popUpRoot.getChildren ().addAll (background, textBox);
		popUpRoot.setLayoutX ((wrapper.content.getWidth () - background
				.getWidth ()) / 2);
		popUpRoot.setLayoutY ((wrapper.content.getHeight () - background
				.getHeight ()) / 2);
		wrapper.content.getChildren ().add (popUpRoot);

		FadeTransition fTrans = new FadeTransition (Duration.seconds (2.5),
				popUpRoot);
		fTrans.setFromValue (background.getOpacity ());
		fTrans.setToValue (0);
		fTrans.setAutoReverse (false);

		TranslateTransition tTrans = new TranslateTransition (
				Duration.seconds (2.5), popUpRoot);
		tTrans.setByY (-30);

		ParallelTransition pTrans = new ParallelTransition (fTrans, tTrans);
		pTrans.setOnFinished (event -> wrapper.content.getChildren ().remove (
				popUpRoot));
		pTrans.setDelay (Duration.seconds (2));
		pTrans.play ();
	}
}