package scoreboard;

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
import javafx.stage.Stage;
import model.Participant;
import model.ParticipantModel;

import org.datafx.controller.FXMLController;

@FXMLController("VoteRegistrator.fxml")
public class VoteRegistrator extends Application implements EventHandler<ActionEvent>,
		Initializable {
	
	@FXML Label voteTopLabel;
	@FXML Button voteConfirmButton;
	
	@FXML ListView<Participant> list12;
	@FXML ListView<Participant> list10;
	@FXML ListView<Participant> list8;
	@FXML ListView<Participant> list7;
	@FXML ListView<Participant> list6;
	@FXML ListView<Participant> list5;
	@FXML ListView<Participant> list4;
	@FXML ListView<Participant> list3;
	@FXML ListView<Participant> list2;
	@FXML ListView<Participant> list1;
	
	ParticipantModel pModel;
	Participant currentVoter;
	
	public static void main(String[] args) {
		launch (args);
	}

	public VoteRegistrator (ParticipantModel pModel, Participant p) {
		this.pModel = pModel;
		currentVoter = p;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		System.out.println (currentVoter.getName ());
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation (getClass ().getResource ("VoteRegistrator.fxml"));
		Pane voteDocRoot = (Pane) loader.load ();	
			
		stage.setScene (new Scene (voteDocRoot));
		stage.show();
	}

	@Override
	public void handle (ActionEvent event) {
		try {
			start (new Stage());
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
}