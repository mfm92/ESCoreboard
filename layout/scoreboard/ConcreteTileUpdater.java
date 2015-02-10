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

public class ConcreteTileUpdater extends TileUpdater {

	@Override
	public void updateTiles(Scoreboard scoreboard) {
		Group newSuperNations = new Group();
		for (int position = 0; position < 28; position++){
			
	       	Group nationTile = new Group();
	    	
			Rectangle base = RectangleBuilder.create()
	        	.width(500)
	        	.height(60)
	        	.layoutX(0)
	       		.layoutY(0)
	       		.id("base")
	       		.fill(new ImagePattern((position < 6 ? 
	       				scoreboard.utilities.nationTileBackgroundPQ : 
	       				scoreboard.utilities.nationTileBackground)))
	        	.build();
	        	
	        Rectangle pointsBase = RectangleBuilder.create()
	        	.width(100)
	        	.height(60)
	        	.layoutX(500)
	       		.layoutY(0)
	       		.id("pointBase")
	       		.fill(new ImagePattern(position < 6 ? 
	       			scoreboard.utilities.pointsTileBackgroundPQ : 
	       			scoreboard.utilities.pointsTileBackground))
	      		.build();
	        	
	       	Text nationName = TextBuilder.create()
	        	.text(scoreboard.finalists.get(position).getName().get())
	       		.layoutX(102)
	       		.layoutY(40)
	       		.id("nationName")
	       		.textAlignment(TextAlignment.CENTER)
	        	.font(Font.font("Harabara Mais", FontWeight.MEDIUM, 33))
	       		.fill(position < 6 ? Color.RED : Color.WHITE)
	       		.build();
	        	
	       	Text scoreTest = TextBuilder.create()
	           	.text(new Integer(scoreboard.finalists.get(position).getScore().get()).toString())
	           	.layoutX(546)
	           	.layoutY(40)
	           	.textAlignment(TextAlignment.CENTER)
	           	.font(Font.font("Inconsolata", FontWeight.MEDIUM, 41))
	           	.fill(Color.WHITE)
	           	.build();
	       	
	       	VBox scoreVBox = new VBox();
	       	scoreVBox.setLayoutX(pointsBase.getLayoutX());
	       	scoreVBox.setLayoutY(pointsBase.getLayoutY());
	       	scoreVBox.setPrefHeight(pointsBase.getHeight());
	       	scoreVBox.setPrefWidth(pointsBase.getWidth());
	       	scoreVBox.setAlignment(Pos.CENTER);
	       	scoreVBox.setId("score");
	       	scoreVBox.getChildren().add(scoreTest);
	       	
	       	ImageView nationIcon = ImageViewBuilder.create()
	           	.image(scoreboard.finalists.get(position).getFlag())
	           	.layoutX(10)
	           	.layoutY(8)
	           	.id("icon")
	           	.build();
	       	
	       	nationTile.setLayoutX(100 + 600*((position)/14));
	       	nationTile.setLayoutY(100 + 60*((position)%14));
	       	nationIcon.setFitWidth(73);
	       	nationIcon.setFitHeight(45);
	       	
	       	nationTile.getChildren().clear();
	       	nationTile.getChildren().addAll(base, pointsBase, nationName, scoreVBox, nationIcon);
	       	
	       	if (scoreboard.currentVoter != null) {
		       	if (scoreboard.finalists.get(position).getName().equals(
		       			scoreboard.currentVoter.getName())) {
	       			nationIcon.setImage(scoreboard.utilities.voterPointToken);
	       			base.setFill(new ImagePattern(scoreboard.utilities.nationTileBackgroundVoter));
	       			nationName.setFill(Color.LIGHTGRAY);
	       			scoreTest.setFill(Color.LIGHTGRAY);
	       		}
	       	}
	       	
	       	if (scoreboard.finalists.get(position).getTmpScore() != 0) {
	       		ImageView ptsView = ImageViewBuilder.create()
	       			.image(scoreboard.utilities.getPointsTokens().get(pointsToIndices
	       					(scoreboard.finalists.get(position).getTmpScore())))
	       			.layoutX(10)
	       			.layoutY(8)
	       			.id("ptsProof")
	       			.build();
	       			
	       		ptsView.setFitHeight(45);
	       		ptsView.setFitWidth(73);
	       		
	       		if (scoreboard.finalists.get(position).getScoredFlag() && !(nationName.getText().
	       				endsWith(" has SCORED!") && !(nationName.getText().endsWith(" PQ!")))) {
	       			base.setFill(new ImagePattern(scoreboard.utilities.nationTileBackgroundScored));
	       			if (position < 6) {
	       				base.setFill(new ImagePattern(scoreboard.utilities.nationTileBackgroundPQScored));
	       			}
	       		}	       		
	       		nationTile.getChildren().add(ptsView);
	       	}
	       	
	       	scoreboard.groupNationMap.put(scoreboard.finalists.get(position), nationTile);
	       	newSuperNations.getChildren().add(nationTile);
		}
		scoreboard.root.getChildren().remove(scoreboard.root.lookup("#nations"));
		if (scoreboard.root.getChildren().size() >= 1) 
			scoreboard.root.getChildren().add(1, newSuperNations);
		newSuperNations.setId("nations");
		scoreboard.superNations = newSuperNations;
	}
	
    private int pointsToIndices (int pts) {
    	if (pts <= 8) return pts;
    	if (pts == 10) return 9;
    	if (pts == 12) return 0;
    	
    	System.out.println("pointsToIndices returns sth baaaad, pts was: " + pts);
    	
    	return 103859; //absolutely pointless huehue
    }

	@Override
	public void updateBackgroundOnly (Scoreboard scoreboard) {
		for (int position = 0; position < 28; position++){
			
			Group group = scoreboard.groupNationMap.get(scoreboard.finalists.get(position));
	    	
			Rectangle base = RectangleBuilder.create()
	        	.width(500)
	        	.height(60)
	        	.layoutX(0)
	       		.layoutY(0)
	       		.id("base")
	       		.fill(new ImagePattern((position < 6 ? 
	       				scoreboard.utilities.nationTileBackgroundPQ : 
	       				scoreboard.utilities.nationTileBackground)))
	        	.build();
			
			group.getChildren().remove(group.getChildren().get(0));
			group.getChildren().add(0, base);
		}
	}

}