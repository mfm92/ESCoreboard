package scoreboard;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBuilder;
import nations.Participant;

public class ConcreteTileUpdater extends AbstractTileUpdater {

	@Override
	public void prettyFormatting (final Scoreboard scoreboard, int nrOfPart, Participant receiver) {

		final int sizeDenom = (int) Math.ceil ((double) nrOfPart
				/ (double) scoreboard.columnsNr);
		Group newSuperNations = new Group ();

		for (int position = 0; position < nrOfPart; position++) {

			final Group nationTile = new Group ();

			Rectangle base = RectangleBuilder
					.create ()
					.width (scoreboard.columnNameWidth)
					.height (scoreboard.height / sizeDenom)
					.layoutX (0)
					.layoutY (0)
					.id ("base")
					.fill (new ImagePattern (
							(position < scoreboard.specialBorder ? scoreboard.utilities.nationTileBackgroundPQ
									: scoreboard.utilities.nationTileBackground)))
					.build ();

			Rectangle pointsBase = RectangleBuilder
					.create ()
					.width (scoreboard.columnWidth - scoreboard.columnNameWidth)
					.height (scoreboard.height / sizeDenom)
					.layoutX (scoreboard.columnNameWidth)
					.layoutY (0)
					.id ("pointBase")
					.fill (new ImagePattern (
							position < scoreboard.specialBorder ? scoreboard.utilities.pointsTileBackgroundPQ
									: scoreboard.utilities.pointsTileBackground))
					.build ();

			Text nationName = TextBuilder
					.create ()
					.text (scoreboard.participants.get (position).getName ())
					.layoutX (
							scoreboard.flagWidth
									+ scoreboard.nameFromFlagOffset)
					.layoutY (0.65 * pointsBase.getHeight ())
					.id ("nationName")
					.textAlignment (TextAlignment.CENTER)
					.font (Font.font ("Harabara Mais", FontWeight.MEDIUM, 33))
					.fill (position < scoreboard.specialBorder ? Color.RED
							: Color.WHITE).build ();

			Text scoreTest = TextBuilder
					.create ()
					.text (new Integer (scoreboard.participants.get (position)
							.getScore ()).toString ())
					.layoutX (base.getWidth () + 46).layoutY (40)
					.textAlignment (TextAlignment.CENTER)
					.font (Font.font ("Inconsolata", FontWeight.MEDIUM, 41))
					.fill (Color.WHITE).build ();

			VBox scoreVBox = new VBox ();
			scoreVBox.setLayoutX (pointsBase.getLayoutX ());
			scoreVBox.setLayoutY (pointsBase.getLayoutY ());
			scoreVBox.setPrefHeight (pointsBase.getHeight ());
			scoreVBox.setPrefWidth (pointsBase.getWidth ());
			scoreVBox.setAlignment (Pos.CENTER);
			scoreVBox.setId ("score");
			scoreVBox.getChildren ().add (scoreTest);

			ImageView nationIcon = ImageViewBuilder.create ()
					.image (scoreboard.participants.get (position).getFlag ())
					.layoutX ((base.getHeight () - scoreboard.flagHeight) / 2)
					.layoutY ((base.getHeight () - scoreboard.flagHeight) / 2)
					.id ("icon").build ();

			nationTile.setLayoutX (scoreboard.globalXOffset
					+ scoreboard.columnWidth * (position / sizeDenom));
			nationTile.setLayoutY (scoreboard.globalYOffset
					+ (scoreboard.height / sizeDenom) * (position % sizeDenom));

			nationIcon.setFitWidth (scoreboard.flagWidth);
			nationIcon.setFitHeight (scoreboard.flagHeight);

			nationTile.getChildren ().clear ();
			nationTile.getChildren ().addAll (base, pointsBase, nationName,
					scoreVBox, nationIcon);

			if (scoreboard.currentVoter != null) {
				if (scoreboard.participants.get (position).getName ()
						.equals (scoreboard.currentVoter.getName ())) {
					nationIcon.setImage (scoreboard.utilities.voterPointToken);
					base.setFill (new ImagePattern (
							scoreboard.utilities.nationTileBackgroundVoter));
					nationName.setFill (Color.LIGHTGRAY);
					scoreTest.setFill (Color.LIGHTGRAY);
				}
			}

			if (scoreboard.participants.get (position).getTmpScore () != 0) {
				ImageView ptsView = ImageViewBuilder
						.create ()
						.image (scoreboard.utilities
								.getPointsTokens ()
								.get (scoreboard
										.pointsToIndices (scoreboard.participants
												.get (position).getTmpScore ())))
						.layoutX (nationIcon.getLayoutX ())
						.layoutY (nationIcon.getLayoutY ()).id ("ptsProof")
						.build ();

				ptsView.setFitHeight (nationIcon.getFitHeight ());
				ptsView.setFitWidth (nationIcon.getFitWidth ());

				if (scoreboard.participants.get (position).getScoredFlag ()
						&& !(nationName.getText ().endsWith (" has SCORED!") && !(nationName
								.getText ().endsWith (" PQ!")))) {
					base.setFill (new ImagePattern (
							scoreboard.utilities.nationTileBackgroundScored));
					if (position < scoreboard.specialBorder) {
						base.setFill (new ImagePattern (
								scoreboard.utilities.nationTileBackgroundPQScored));
					}
				}
				
				if (scoreboard.participants.get (position) == receiver) {
					// do some effects here...
				}

				nationTile.getChildren ().add (ptsView);
			}

			scoreboard.groupNationMap.put (
					scoreboard.participants.get (position), nationTile);
			newSuperNations.getChildren ().add (nationTile);
		}

		scoreboard.root.getChildren ().remove (
				scoreboard.root.lookup ("#nations"));
		if (scoreboard.root.getChildren ().size () >= 1)
			scoreboard.root.getChildren ().add (1, newSuperNations);
		newSuperNations.setId ("nations");
		scoreboard.superNations = newSuperNations;
	}
}
