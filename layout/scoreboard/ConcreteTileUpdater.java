package scoreboard;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import nations.Participant;

public class ConcreteTileUpdater extends AbstractTileUpdater {

	@Override
	public void prettyFormatting (final Scoreboard scoreboard, int nrOfPart, Participant receiver) {

		final int sizeDenom = (int) Math.ceil ((double) nrOfPart
				/ (double) scoreboard.getColumnsNr());
		Group newSuperNations = new Group ();

		for (int position = 0; position < nrOfPart; position++) {

			final Group nationTile = new Group ();

			Rectangle base = new Rectangle();
			base.setWidth (scoreboard.getColumnNameWidth ());
			base.setHeight (scoreboard.getHeight() / sizeDenom);
			base.setLayoutX (0);
			base.setLayoutY (0);
			base.setId ("base");
			base.setFill (new ImagePattern (
							(position < scoreboard.getSpecialBorder() ? scoreboard.getDataCarrier().nationTileBackgroundPQ
									: scoreboard.getDataCarrier().nationTileBackground)));

			Rectangle pointsBase = new Rectangle();
			pointsBase.setWidth (scoreboard.getColumnWidth() - scoreboard.getColumnNameWidth());
			pointsBase.setHeight (scoreboard.getHeight() / sizeDenom);
			pointsBase.setLayoutX (scoreboard.getColumnNameWidth ());
			pointsBase.setLayoutY (0);
			pointsBase.setId ("pointBase");
			pointsBase.setFill (new ImagePattern (
							position < scoreboard.getSpecialBorder() ? scoreboard.getDataCarrier().pointsTileBackgroundPQ
									: scoreboard.getDataCarrier().pointsTileBackground));

			Text nationName = new Text();
			nationName.setText (scoreboard.getParticipants().get (position).getName ());
			nationName.setLayoutX (scoreboard.getFlagWidth()
					+ scoreboard.getNameFromFlagOffset());
			nationName.setLayoutY (0.65 * pointsBase.getHeight ());
			nationName.setId ("nationName");
			nationName.setTextAlignment (TextAlignment.CENTER);
			nationName.setFont (Font.font ("Harabara Mais", FontWeight.MEDIUM, 33));
			nationName.setFill (position < scoreboard.getSpecialBorder() ? Color.RED
					: Color.WHITE);

			Text scoreTest = new Text();
			scoreTest.setText (new Integer (scoreboard.getParticipants().get (position)
							.getScore ()).toString ());
			scoreTest.setLayoutX (base.getWidth () + 46);
			scoreTest.setLayoutY (40);
			scoreTest.setTextAlignment (TextAlignment.CENTER);
			scoreTest.setFont (Font.font ("Inconsolata", FontWeight.MEDIUM, 41));
			scoreTest.setFill (Color.WHITE);

			VBox scoreVBox = new VBox ();
			scoreVBox.setLayoutX (pointsBase.getLayoutX ());
			scoreVBox.setLayoutY (pointsBase.getLayoutY ());
			scoreVBox.setPrefHeight (pointsBase.getHeight ());
			scoreVBox.setPrefWidth (pointsBase.getWidth ());
			scoreVBox.setAlignment (Pos.CENTER);
			scoreVBox.setId ("score");
			scoreVBox.getChildren ().add (scoreTest);

			ImageView nationIcon = new ImageView();
			nationIcon.setImage (scoreboard.getParticipants().get (position).getFlag ());
			nationIcon.setLayoutX ((base.getHeight () - scoreboard.getFlagHeight()) / 2);
			nationIcon.setLayoutY ((base.getHeight () - scoreboard.getFlagHeight()) / 2);
			nationIcon.setId ("icon");

			nationTile.setLayoutX (scoreboard.getGlobalXOffset()
					+ scoreboard.getColumnWidth() * (position / sizeDenom));
			nationTile.setLayoutY (scoreboard.getGlobalYOffset()
					+ (scoreboard.getHeight() / sizeDenom) * (position % sizeDenom));

			nationIcon.setFitWidth (scoreboard.getFlagWidth());
			nationIcon.setFitHeight (scoreboard.getFlagHeight());

			nationTile.getChildren ().clear ();
			nationTile.getChildren ().addAll (base, pointsBase, nationName,
					scoreVBox, nationIcon);

			if (scoreboard.getCurrentVoter() != null) {
				if (scoreboard.getParticipants().get (position).getName ()
						.equals (scoreboard.getCurrentVoter().getName ())) {
					nationIcon.setImage (scoreboard.getDataCarrier().voterPointToken);
					base.setFill (new ImagePattern (
							scoreboard.getDataCarrier().nationTileBackgroundVoter));
					nationName.setFill (Color.LIGHTGRAY);
					scoreTest.setFill (Color.LIGHTGRAY);
				}
			}

			if (scoreboard.getParticipants().get (position).getTmpScore () != 0) {
				ImageView ptsView = new ImageView();
				ptsView.setImage (scoreboard.getDataCarrier().getPointsTokens ().get (scoreboard
						.pointsToIndices (scoreboard.getParticipants().get (position).getTmpScore ())));
				ptsView.setLayoutX (nationIcon.getLayoutX ());
				ptsView.setLayoutY (nationIcon.getLayoutY ());
				ptsView.setId("ptsProof");

				ptsView.setFitHeight (nationIcon.getFitHeight ());
				ptsView.setFitWidth (nationIcon.getFitWidth ());

				if (scoreboard.getParticipants().get (position).getScoredFlag ()
						&& !(nationName.getText ().endsWith (" has SCORED!") && !(nationName
								.getText ().endsWith (" PQ!")))) {
					base.setFill (new ImagePattern (
							scoreboard.getDataCarrier().nationTileBackgroundScored));
					if (position < scoreboard.getSpecialBorder()) {
						base.setFill (new ImagePattern (
								scoreboard.getDataCarrier().nationTileBackgroundPQScored));
					}
				}
				
				if (scoreboard.getParticipants().get (position) == receiver) {
					// do some effects here...
				}

				nationTile.getChildren ().add (ptsView);
			}

			scoreboard.getGroupNationMap().put (
					scoreboard.getParticipants().get (position), nationTile);
			newSuperNations.getChildren ().add (nationTile);
		}

		scoreboard.getRoot().getChildren ().remove (
				scoreboard.getRoot().lookup ("#nations"));
		if (scoreboard.getRoot().getChildren ().size () >= 1)
			scoreboard.getRoot().getChildren ().add (1, newSuperNations);
		newSuperNations.setId ("nations");
		scoreboard.setSuperNations (newSuperNations);
	}
}
