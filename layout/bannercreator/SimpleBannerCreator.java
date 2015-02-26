package bannercreator;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;

import nations.Participant;
import scoreboard.Scoreboard;

import com.sun.istack.internal.logging.Logger;

public class SimpleBannerCreator extends BannerCreator {

	public SimpleBannerCreator(int height, int width) {
		super (height, width);
	}

	@Override
	public void createBanners(Participant p, String startStatus,
			String startGrid) {
		Node banner = makeLayout (p, startStatus, startGrid);
		BufferedImage image = new BufferedImage (bannerWidth, bannerHeight,
				BufferedImage.TYPE_4BYTE_ABGR);
		WritableImage bannerImage = banner.snapshot (new SnapshotParameters (),
				null);
		BufferedImage bannerNextStep = SwingFXUtils.fromFXImage (bannerImage,
				image);
		File bannerDestination = new File (System.getProperty ("user.dir") + "\\banners\\"
						+ p.getName () + ".png");
		bannerDestination.getParentFile ().mkdirs ();

		try {
			Graphics2D g2D = (Graphics2D) bannerNextStep.getGraphics ();
			g2D.translate (bannerHeight, bannerWidth);
			ImageIO.write (bannerNextStep, "png", bannerDestination);
		} catch (IOException ioex) {
			Logger.getLogger (Scoreboard.class).log (Level.SEVERE,
					ioex.getMessage ());
		}
	}

	private Node makeLayout(Participant p, String startStatus, String startGrid) {

		Group bannerRoot = new Group ();
		Rectangle bannerBackground = new Rectangle (bannerHeight, bannerWidth);

		Color background = null;
		Color textColor = null;
		Color startGridBG = null;
		Color startGridFG = null;

		switch (startStatus) {
		case "SF1":
			background = Color.DARKBLUE;
			textColor = Color.LIGHTGRAY;
			startGridBG = Color.DARKRED;
			startGridFG = Color.LIGHTGRAY;
			break;
		case "SF2":
			background = Color.DARKRED;
			textColor = Color.LIGHTGRAY;
			startGridBG = Color.DARKBLUE;
			startGridFG = Color.LIGHTGRAY;
			break;
		case "PQ":
			background = Color.LIGHTGRAY;
			textColor = Color.DARKBLUE;
			startGridBG = Color.DARKBLUE;
			startGridFG = Color.LIGHTGRAY;
			break;
		}

		bannerBackground.setFill (background);
		bannerRoot.getChildren ().add (bannerBackground);

		ImageView flag = new ImageView (p.getFlag ());
		flag.setLayoutX (15);
		flag.setLayoutY (15);
		flag.setFitHeight (100);
		flag.setFitWidth (150);

		bannerRoot.getChildren ().add (flag);

		Text partName = new Text (p.getName ());
		partName.setLayoutX (220);
		partName.setLayoutY (40);
		partName.setFill (textColor);
		partName.setFont (Font.font ("Coolvetica RG", partName.getText ()
				.length () > 16 ? 28 : 36));

		Text artistName = new Text (p.getEntry ().getArtist ());
		artistName.setLayoutX (250);
		artistName.setLayoutY (80);
		artistName.setFill (textColor);
		artistName.setFont (Font.font ("Inconsolata", 27));

		Text titleName = new Text (p.getEntry ().getTitle ());
		titleName.setLayoutX (225);
		titleName.setLayoutY (105);
		titleName.setFill (textColor);
		titleName.setFont (Font.font ("Inconsolata", FontPosture.ITALIC, 27));

		VBox partVBox = new VBox ();
		partVBox.setAlignment (Pos.CENTER);
		partVBox.setPrefHeight (50);
		partVBox.setPrefWidth (370);
		partVBox.setLayoutX (165);
		partVBox.setLayoutY (10);
		partVBox.getChildren ().add (partName);

		VBox artistVBox = new VBox ();
		artistVBox.setAlignment (Pos.CENTER);
		artistVBox.setPrefHeight (30);
		artistVBox.setPrefWidth (370);
		artistVBox.setLayoutX (165);
		artistVBox.setLayoutY (60);
		artistVBox.getChildren ().add (artistName);

		VBox titleVBox = new VBox ();
		titleVBox.setAlignment (Pos.CENTER);
		titleVBox.setPrefHeight (30);
		titleVBox.setPrefWidth (370);
		titleVBox.setLayoutX (165);
		titleVBox.setLayoutY (90);
		titleVBox.getChildren ().add (titleName);

		bannerRoot.getChildren ().addAll (partVBox, artistVBox, titleVBox);

		ImageView partImage = new ImageView (p.getFlag ());
		partImage.setLayoutX (bannerHeight - 2 * bannerWidth - 20);
		partImage.setLayoutY (0);
		partImage.setFitHeight (bannerWidth);
		partImage.setFitWidth (bannerWidth + 20);
		bannerRoot.getChildren ().add (partImage);

		Rectangle gridNumber = new Rectangle (bannerWidth, bannerWidth);
		gridNumber.setLayoutX (bannerHeight - bannerWidth);
		gridNumber.setLayoutY (0);
		gridNumber.setFill (startGridBG);

		Text gridText = new Text (startGrid);
		gridText.setLayoutX (bannerHeight - bannerWidth + 50);
		gridText.setLayoutY (50);
		gridText.setFill (startGridFG);
		gridText.setFont (Font.font ("Coolvetica RG", 70));

		VBox gridVBox = new VBox ();
		gridVBox.setAlignment (Pos.CENTER);
		gridVBox.setLayoutX (gridNumber.getLayoutX ());
		gridVBox.setLayoutY (gridNumber.getLayoutY ());
		gridVBox.setPrefHeight (gridNumber.getHeight ());
		gridVBox.setPrefWidth (gridNumber.getWidth ());
		gridVBox.getChildren ().add (gridText);

		bannerRoot.getChildren ().addAll (gridNumber, gridVBox);

		return bannerRoot;
	}
}
