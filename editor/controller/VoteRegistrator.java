package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.ParticipantData;
import controller.commands.VoteSetter;

public class VoteRegistrator extends Application implements Initializable {
	
	@FXML
	Label voteTopLabel;
	@FXML
	Button voteConfirmButton;
	@FXML
	ComboBox<ParticipantData> combo12;
	@FXML
	ComboBox<ParticipantData> combo10;
	@FXML
	ComboBox<ParticipantData> combo8;
	@FXML
	ComboBox<ParticipantData> combo7;
	@FXML
	ComboBox<ParticipantData> combo6;
	@FXML
	ComboBox<ParticipantData> combo5;
	@FXML
	ComboBox<ParticipantData> combo4;
	@FXML
	ComboBox<ParticipantData> combo3;
	@FXML
	ComboBox<ParticipantData> combo2;
	@FXML
	ComboBox<ParticipantData> combo1;
	
	List<ComboBox<ParticipantData>> cmBoxes;
	ObservableList<ParticipantData> ps = FXCollections.observableArrayList ();

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader (getClass ().getResource ("/view/VoteRegistrator.fxml"));
		Pane voteDocRoot = (Pane) loader.load ();
		Scene scene = new Scene(voteDocRoot);
		scene.getStylesheets ().add ("/view/VoteRegistrator.css");
		primaryStage.setScene (scene);
		primaryStage.show ();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		setUpComboBoxes();
		setUpConfirmButton ();
	}
	
	private void setUpComboBoxes () {
		cmBoxes = new LinkedList<>();
		
		cmBoxes.add (combo12);
		cmBoxes.add (combo10);
		cmBoxes.add (combo8);
		cmBoxes.add (combo7);
		cmBoxes.add (combo6);
		cmBoxes.add (combo5);
		cmBoxes.add (combo4);
		cmBoxes.add (combo3);
		cmBoxes.add (combo2);
		cmBoxes.add (combo1);
		
		voteTopLabel.setText ("Votes from...: " + CoreUI.inputData.getSelectedParticipant ().getName ());
		
		for (ParticipantData p : CoreUI.inputData.getParticipants ()) {
			if (p.getStatus ().equals ("P")) ps.add (p);
		}
		
		ArrayList<ParticipantData> previousVotes;
		
		if ((previousVotes = CoreUI.inputData.getVotes ().get (CoreUI.inputData.getSelectedParticipant ())) != null) {
			for (int i = 0; i < cmBoxes.size (); i++) {
				cmBoxes.get (i).getSelectionModel ().select (previousVotes.get (i));
			}	
		}
		
		ps.remove (CoreUI.inputData.getSelectedParticipant ());

		for (ComboBox<ParticipantData> box : cmBoxes) {
			box.setItems (ps);
		}
	}
	
	private void setUpConfirmButton () {
		voteConfirmButton.setOnAction (event -> {
			if (validVotes()) {
				ArrayList<ParticipantData> votes = new ArrayList<>();
				for (ComboBox<ParticipantData> cBox : cmBoxes) {
					votes.add (cBox.getSelectionModel ().getSelectedItem ());
				}
				
				VoteSetter voteSetter = new VoteSetter (CoreUI.inputData.getSelectedParticipant (), votes);
				voteSetter.execute ();
				CoreUI.commandLog.put (++CoreUI.nrOfCommands, voteSetter);
				CoreUI.commandPtr = CoreUI.nrOfCommands;
			}
		});
	}
	
	private boolean validVotes() {
		ArrayList<ParticipantData> votees = new ArrayList<>();
		
		for (ComboBox<ParticipantData> cBox : cmBoxes) {
			ParticipantData votee = cBox.getSelectionModel ().getSelectedItem ();
			if (votee == null) return false;
			votees.add (votee);
		}
		
		for (ParticipantData pData : votees) {
			if (appearsMoreThanOnce (votees, pData)) return false;
		}
		
		return true;
	}
	
	private boolean appearsMoreThanOnce (ArrayList<ParticipantData> votees, ParticipantData searched) {
		int numCount = 0;
		
		for (ParticipantData pData : votees) {
			if (pData.getName ().equals (searched.getName ())) numCount++;
		}
		
		return numCount > 1;
	}
}