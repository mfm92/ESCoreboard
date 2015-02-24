package scoreboard;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import nations.Participant;
import data.Standings;

public abstract class UpdateAnimator {
	
	public abstract void updateAnimate (final Scoreboard scoreboard,
			final Participant voter, final Standings overview,
			final ArrayList<Participant> oldStandings,
			final ArrayList<Participant> standings);
	
	void countUpScore (Scoreboard scoreboard, Participant receiver) {
    	//SET CORRECT SCORE
    	int newNrOfPoints = receiver.getScore().get();
    	Group nationGroup = scoreboard.groupNationMap.get(receiver);
    	double xProperty = nationGroup.getChildren().get(3).getLayoutX();
    	double yProperty = nationGroup.getChildren().get(3).getLayoutY();
    	double vHeight = ((VBox)(nationGroup.getChildren().get(3))).getHeight();
    	double vWidth = ((VBox)(nationGroup.getChildren().get(3))).getWidth();
    	
        //COUNT IT UP
        nationGroup.getChildren().remove(nationGroup.lookup("#score"));
               		
        Text scoreTest = TextBuilder.create()
        		.text(new Integer(newNrOfPoints).toString())
        		.font(Font.font("Inconsolata", FontWeight.MEDIUM, 38))
        		.fill(Color.WHITE)
        		.build();
        
        VBox scoreVBox = new VBox();
       	scoreVBox.setLayoutX(xProperty);
       	scoreVBox.setLayoutY(yProperty);
       	scoreVBox.setPrefHeight(vHeight);
       	scoreVBox.setPrefWidth(vWidth);
       	scoreVBox.setAlignment(Pos.CENTER);
       	scoreVBox.getChildren().add(scoreTest);
       	
        nationGroup.getChildren().add(3, scoreVBox);
    }
	
	double getXCoordByPos (int position, int parts) {
		return 100 + 600*((position-1)/((parts+1) / 2));
	}

	double getYCoordByPos (int position, int parts) {
		return 100 + (840/((parts+1) / 2)) * ((position-1) % ((parts+1) / 2));
	}
}