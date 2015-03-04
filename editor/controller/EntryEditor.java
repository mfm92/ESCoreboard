package controller;

import java.net.URL;
import java.util.ResourceBundle;

import model.ParticipantData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class EntryEditor implements Initializable {
	
	@FXML TextField nameField;
	@FXML TextField artistField;
	@FXML TextField titleField;
	@FXML TextField shortNameField;
	@FXML TextField startField;
	@FXML TextField startTimeField;
	@FXML TextField endTimeField;
	@FXML TextField statusField;

	@FXML Button confirmButton;
	
	ParticipantData toBeEdited;
	
	public void init (Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader (getClass ().getResource ("/view/EntryAdder.fxml"));
		Pane voteDocRoot = (Pane) loader.load ();
		toBeEdited = CoreUI.inputData.getSelectedParticipant ();
		primaryStage.setScene (new Scene (voteDocRoot));
		primaryStage.show ();
	}
	
	private void setUp () {
		setUpTextFields ();
		setUpConfirmButton ();
	}
	
	private void setUpTextFields() {
		nameField.setText (toBeEdited.getName ());
		shortNameField.setText (toBeEdited.getShortName ());
		artistField.setText (toBeEdited.getArtist ());
		titleField.setText (toBeEdited.getTitle ());
		startField.setText (toBeEdited.getGrid () + "");
		startTimeField.setText (toBeEdited.getStart () + "");
		endTimeField.setText (toBeEdited.getStop () + "");
		statusField.setText (toBeEdited.getStatus ());
	}
	
	private void setUpConfirmButton () {
		confirmButton.setOnAction (new EventHandler<ActionEvent>() {
			
			@Override
			public void handle (ActionEvent event) {
				if (checkValidity ()) {
					ParticipantData nParticipant = new ParticipantData 
							(nameField.getText (), 
							shortNameField.getText (), 
							artistField.getText (), 
							titleField.getText (), 
							Integer.parseInt (startTimeField.getText ()), 
							Integer.parseInt (endTimeField.getText ()), 
							Integer.parseInt (startField.getText ()), 
							statusField.getText ());
					
					CoreUI.inputData.addParticipant (nParticipant);
					CoreUI.commandLog.put (++CoreUI.nrOfCommands, new Pair<CoreUI.Command, ParticipantData> 
						(CoreUI.Command.ADD, nParticipant));
					CoreUI.commandPtr = CoreUI.nrOfCommands;
				}
			}
		});
	}
	
	private boolean checkValidity () {
		if (nameField.getText ().equals ("")) return false;
		if (artistField.getText ().equals ("")) return false;
		if (titleField.getText ().equals ("")) return false;
		if (startField.getText ().equals ("")) return false;
		if (endTimeField.getText ().equals ("")) return false;
		if (startTimeField.getText ().equals ("")) return false;
		if (statusField.getText ().equals ("")) return false;
		if (shortNameField.getText ().equals ("")) return false;
		
		return true;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setUp();
	}
}