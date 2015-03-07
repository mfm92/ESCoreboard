package controller;

import java.net.URL;
import java.util.ResourceBundle;

import model.ParticipantData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import controller.commands.EntryAdder;

public class EntryAdderUI implements Initializable {
	
	@FXML
	public TextField nameField;
	@FXML
	public TextField artistField;
	@FXML
	public TextField titleField;
	@FXML
	public TextField shortNameField;
	@FXML
	public TextField startField;
	@FXML
	public TextField startTimeField;
	@FXML
	public TextField endTimeField;
	@FXML
	public TextField statusField;

	@FXML Button confirmButton;
	
	public void init (Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader (getClass ().getResource ("/view/EntryAdder.fxml"));
		Pane voteDocRoot = (Pane) loader.load ();
		Scene addScene = new Scene (voteDocRoot);
		addScene.getStylesheets ().add("/view/EntryAdder.css");
		primaryStage.setScene (addScene);
		primaryStage.show ();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setUp();
	}

	private void setUp () {
		setUpConfirmButton ();
	}
	
	private void setUpConfirmButton () {
		confirmButton.setOnAction (event -> {
			if (checkValidity()) {
				ParticipantData target = new ParticipantData 
						(nameField.getText (), 
								shortNameField.getText (), 
								artistField.getText (), 
								titleField.getText (), 
								startTimeField.getText (), 
								endTimeField.getText (), 
								startField.getText (), 
								statusField.getText ());
				
				EntryAdder adder = new EntryAdder (target);
				adder.execute ();
				CoreUI.commandLog.put (++CoreUI.nrOfCommands, adder);
				CoreUI.commandPtr = CoreUI.nrOfCommands;
			}
		});
	}
	
	private boolean checkValidity () {
		if (nameField.getText ().equals ("")) return false;
		if (artistField.getText ().equals ("")) return false;
		if (titleField.getText ().equals ("")) return false;
		if (endTimeField.getText ().equals ("")) return false;
		if (shortNameField.getText ().equals ("")) return false;
		
		return true;
	}
}