package scoreboard;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VoteRegistrator extends Application implements EventHandler<ActionEvent>,
		Initializable {
	
	@FXML Label voteTopLabel;
	@FXML Button voteConfirmButton;
	
	@FXML ListView<ParticipantSave> List12;
	@FXML ListView<ParticipantSave> List10;
	@FXML ListView<ParticipantSave> List8;
	@FXML ListView<ParticipantSave> List7;
	@FXML ListView<ParticipantSave> List6;
	@FXML ListView<ParticipantSave> List5;
	@FXML ListView<ParticipantSave> List4;
	@FXML ListView<ParticipantSave> List3;
	@FXML ListView<ParticipantSave> List2;
	@FXML ListView<ParticipantSave> List1;
	
	Stage primaryStage;
	
	public static void main(String[] args) {
		launch (args);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(Stage stage) throws Exception {
		primaryStage = stage;
	}

	@Override
	public void handle (ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation (getClass ().getResource ("VoteDoc.fxml"));
			Pane voteDocRoot = (Pane) loader.load ();	
			Stage voteStage = new Stage();
			voteStage.initModality (Modality.APPLICATION_MODAL);
			voteStage.setScene (new Scene (voteDocRoot));
			voteStage.showAndWait ();
		} catch (IOException ioex) {
			ioex.printStackTrace ();
		}
	}

}
