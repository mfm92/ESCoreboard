package bannercreator;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;

import nations.Participant;
import utilities.Utilities;

public class ESCBannerCreator extends BannerCreator {

	public ESCBannerCreator (int bannerWidth, int bannerHeight) {
		super (bannerWidth, bannerHeight);
	}

	@Override
	public void createBanners (Participant p, String startStatus, String startGrid) throws IOException {
		Image background = Utilities.readImage ("resources/Graphics/banners/bannerbase.png");
		
		Group bRoot = new Group ();
		bRoot.getChildren ().add (new Rectangle (background.getWidth (), background.getHeight ()));
		
		ImageView flagBase = new ImageView (p.getFlag ());
		flagBase.setFitHeight (0.7875 * background.getHeight ());
		flagBase.setFitWidth (((2352d - 1620d)/2360d) * background.getWidth ());
		flagBase.setLayoutX ((1620d/2360d) * background.getWidth ());
		flagBase.setLayoutY (0.125 * background.getHeight ());
		
		bRoot.getChildren ().add (flagBase);
		bRoot.getChildren ().add (new ImageView(background));

		Text nationText = new Text(p.getName ());
		nationText.setFill (Color.WHITE);
		nationText.setFont (Font.font ("Roboto Lt", FontWeight.BOLD, 119));
		
		VBox nationBox = new VBox ();
		nationBox.setPrefWidth (0.6 * background.getWidth ());
		nationBox.setPrefHeight (0.2 * background.getHeight ());
		nationBox.setLayoutX (0.13 * background.getWidth ());
		nationBox.setLayoutY (0.16 * background.getHeight());
		nationBox.setAlignment (Pos.CENTER);
		nationBox.getChildren ().add (nationText);
		bRoot.getChildren ().add (nationBox);
		
		Text arText = new Text (p.getEntry ().getArtist ());
		arText.setFill (Color.WHITE);
		arText.setFont (Font.font ("Roboto Lt", FontWeight.MEDIUM, arText.getText ().length () > 23 ? 70 : 90));
		
		VBox arBox = new VBox();
		arBox.setPrefWidth (nationBox.getPrefWidth ());
		arBox.setLayoutX (nationBox.getLayoutX ());
		arBox.setPrefHeight (0.3 * background.getHeight ());
		arBox.setLayoutY (0.44 * background.getHeight ());
		arBox.setAlignment (Pos.CENTER);
		arBox.getChildren().add(arText);
		bRoot.getChildren ().add (arBox);
		
		Text tiText = new Text ("\"" + p.getEntry ().getTitle () + "\"");
		tiText.setFill (Color.LIGHTGRAY);
		tiText.setFont (Font.font ("Inconsolata", FontWeight.THIN, tiText.getText ().length () > 23 ? 70 : 90));
		
		VBox tiBox = new VBox();
		tiBox.setPrefWidth (nationBox.getPrefWidth ());
		tiBox.setLayoutX (nationBox.getLayoutX ());
		tiBox.setPrefHeight (0.3 * background.getHeight ());
		tiBox.setLayoutY (0.61 * background.getHeight ());
		tiBox.setAlignment (Pos.CENTER);
		tiBox.getChildren().add (tiText);
		bRoot.getChildren ().add (tiBox);
		
		Text scoreText = new Text (startGrid);
		scoreText.setFill (Color.WHITE);
		scoreText.setFont (Font.font ("Coolvetica RG", FontWeight.BOLD, 156));
		
		VBox scoreBox = new VBox ();
		scoreBox.setPrefWidth (((361d-57d)/2350d) * background.getWidth());
		scoreBox.setPrefHeight (((456d-166d)/650d) * background.getHeight());
		scoreBox.setLayoutX (((57d)/2350d) * background.getWidth());
		scoreBox.setLayoutY (((166d)/650d) * background.getHeight());
		scoreBox.setAlignment (Pos.CENTER);
		scoreBox.getChildren ().add (scoreText);
		bRoot.getChildren ().add (scoreBox);
		
		BufferedImage image = new BufferedImage ((int) background.getWidth(), (int) background.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		WritableImage bannerImage = bRoot.snapshot (new SnapshotParameters (), null);
		BufferedImage bannerNextStep = SwingFXUtils.fromFXImage (bannerImage, image);
		File bannerBase = new File (System.getProperty ("user.dir") + "/banners/" + p.getName () + ".png");
		bannerBase.getParentFile ().mkdirs ();
		
		CropImageFilter cif = new CropImageFilter (0, 0, (int) background.getWidth (), (int) background.getHeight ());
		Toolkit.getDefaultToolkit ().createImage (new FilteredImageSource (bannerNextStep.getSource (), cif));
		
		try {
			Graphics2D g2D = (Graphics2D) bannerNextStep.getGraphics ();
			g2D.translate (background.getHeight(), background.getWidth());
			ImageIO.write (bannerNextStep, "png", bannerBase);
		} catch (IOException ioex) {
			ioex.printStackTrace ();
		}
	}
}
