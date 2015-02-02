package scoreboard;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import nations.Participant;

public class ConcreteVoteSideBarCreator extends VoteSideBarCreator {

	@Override
	public void makeSideOfScoreboard(Group group, Participant voter, Scoreboard scoreboard) {
		group.getChildren().remove(group.lookup("#sidebar"));
		Group sidebar = new Group();
		sidebar.setId("sidebar");
		
		double topBar = scoreboard.groupNationMap.get(scoreboard.finalists.get(0)).getLayoutY();
		double bottomBar = scoreboard.groupNationMap.get(scoreboard.finalists.get(13)).getLayoutY() + 60;;
		
		Rectangle underlay = RectangleBuilder.create()
			.width(520)
			.height(bottomBar - topBar)
			.x(1350)
			.y(topBar)
			.fill(new Color(0.5, 0.5, 0.5, 0.5))
			.build();
		
		Text currentVoter = TextBuilder.create()
			.text("voting now: ")
			.fill(Color.WHITE)
			.font(Font.font("Coolvetica RG", FontWeight.SEMI_BOLD, voter.getName().get().length() < 14 ? 82 : 58))
			.x(1450)
			.y(0)
			.build();
		
		VBox currentVoterVBox = VBoxBuilder.create()
			.layoutX(1350)
			.layoutY(topBar + 20)
			.prefHeight(100)
			.prefWidth(520)
			.alignment(Pos.CENTER)
			.build();
		
		currentVoterVBox.getChildren().add(currentVoter);
		
		Text currentVoter2 = TextBuilder.create()
			.text(voter.getName().get())
			.fill(Color.WHITE)
			.font(Font.font("Coolvetica RG", FontWeight.SEMI_BOLD, voter.getName().get().length() < 14 ? 82 : 58))
			.x(1460)
			.y(800)
			.build();
		
		VBox currentVoter2VBox = VBoxBuilder.create()
				.layoutX(1350)
				.layoutY(770)
				.prefHeight(200)
				.prefWidth(520)
				.alignment(Pos.CENTER)
				.build();
		
		currentVoter2VBox.getChildren().add(currentVoter2);
		
		sidebar.getChildren().addAll(underlay, currentVoterVBox, currentVoter2VBox);
		
		ImageView voterFlag = ImageViewBuilder.create()
				.image(scoreboard.utilities.diamondMap.get(voter))
				.layoutX(1370)
				.layoutY(260)
				.fitWidth(500)
				.fitHeight(550)
				.build();
		
		HBox diamondVBox = new HBox();
		diamondVBox.setLayoutX(1350);
		diamondVBox.setLayoutY(260);
		diamondVBox.setPrefHeight(550);
		diamondVBox.setPrefWidth(520);
		diamondVBox.setAlignment(Pos.CENTER);
		diamondVBox.getChildren().add(voterFlag);
			
		sidebar.getChildren().add(diamondVBox);
		group.getChildren().add(sidebar);
	}
}
