package scoreboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Participant;

public class VoteRegistrator extends Application implements Initializable {
	
	@FXML
	Label voteTopLabel;
	@FXML
	Button voteConfirmButton;
	@FXML
	ComboBox<String> combo12;
	@FXML
	ComboBox<String> combo10;
	@FXML
	ComboBox<String> combo8;
	@FXML
	ComboBox<String> combo7;
	@FXML
	ComboBox<String> combo6;
	@FXML
	ComboBox<String> combo5;
	@FXML
	ComboBox<String> combo4;
	@FXML
	ComboBox<String> combo3;
	@FXML
	ComboBox<String> combo2;
	@FXML
	ComboBox<String> combo1;
	
	List<ComboBox<String>> cmBoxes;

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader (getClass ().getResource ("VoteRegistrator.fxml"));
		Pane voteDocRoot = (Pane) loader.load ();
		primaryStage.setScene (new Scene (voteDocRoot));
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
		
		voteTopLabel.setText ("Votes from...: " + CoreUI.pModel.getSelectedParticipant ().getName ());
		
		ArrayList<String> previousVotes;
		
		if ((previousVotes = CoreUI.pModel.getVotes ().get (CoreUI.pModel.getSelectedParticipant ())) != null) {
			for (int i = 0; i < cmBoxes.size (); i++) {
				cmBoxes.get (i).getSelectionModel ().select (previousVotes.get (i));
			}	
		}
		
		ObservableList<String> ps = FXCollections.observableArrayList ();
		
		for (Participant p : CoreUI.pModel.getParticipants ()) {
			ps.add (p.getName ());
		}
		
		ps.remove (CoreUI.pModel.getSelectedParticipant ().getName ());

		for (ComboBox<String> box : cmBoxes) {
			box.setItems (ps);
		}
	}
	
	private void setUpConfirmButton () {
		voteConfirmButton.setOnAction (new EventHandler<ActionEvent>() {
			@Override
			public void handle (ActionEvent event) {
				if (validVotes()) {
					ArrayList<String> votes = new ArrayList<>();
					for (ComboBox<String> cBox : cmBoxes) {
						votes.add (cBox.getSelectionModel ().getSelectedItem ());
					}
					CoreUI.pModel.addVotes (CoreUI.pModel.getSelectedParticipant (), votes);
				}
			}
		});
	}
	
	private boolean validVotes() {
		ArrayList<String> votees = new ArrayList<>();
		
		for (ComboBox<String> cBox : cmBoxes) {
			String votee = cBox.getSelectionModel ().getSelectedItem ();
			if (votee == null) return false;
			votees.add (votee);
		}
		
		for (String s : votees) {
			if (appearsMoreThanOnce (votees, s)) return false;
		}
		
		return true;
	}
	
	private boolean appearsMoreThanOnce (ArrayList<String> list, String searched) {
		int numCount = 0;
		
		for (String s : list) {
			if (s.equals(searched)) numCount++;
		}
		
		return numCount > 1;
	}
}