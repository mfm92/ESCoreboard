package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.ParticipantData;

import org.apache.commons.lang3.math.NumberUtils;

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
		
		double originalHeight = voteDocRoot.getHeight ();
		double originalWidth = voteDocRoot.getWidth ();
		
		if (Pane.USE_COMPUTED_SIZE == voteDocRoot.getPrefHeight ()) {
			voteDocRoot.setPrefHeight (originalHeight);
		} else originalHeight = voteDocRoot.getPrefHeight ();
		
		if (Pane.USE_COMPUTED_SIZE == voteDocRoot.getPrefWidth ()) {
			voteDocRoot.setPrefWidth (originalWidth);
		} else originalWidth = voteDocRoot.getPrefWidth ();
		
		Group wrapper = new Group (voteDocRoot);
		StackPane sPane = new StackPane ();
		sPane.getChildren ().add (wrapper);
		
		Scene scene = new Scene (sPane);
	
		wrapper.scaleXProperty ().bind (scene.widthProperty ().divide (originalWidth));
		wrapper.scaleYProperty ().bind (scene.heightProperty ().divide (originalHeight));
		
		scene.getStylesheets ().add("/view/EntryAdder.css");
		primaryStage.setScene (scene);
		primaryStage.show ();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setUpConfirmButton ();
		setUpTextFields ();
	}
	
	private void setUpTextFields () {		
		artistField.textProperty ().addListener (event -> {
			if (validArtist (artistField.getText ())) {
				removeStyle (artistField, "text-field-invalid");
				artistField.getStyleClass().add("text-field-valid");
			} else {
				removeStyle (artistField, "text-field-valid");
				if (notEmpty (artistField.getText())) {
					artistField.getStyleClass().add("text-field-invalid");
				} else {
					removeStyle (artistField, "text-field-invalid");
				}
			}
		});
		
		nameField.textProperty ().addListener (event -> {
			if (validName (nameField.getText ())) {
				removeStyle (nameField, "text-field-invalid");
				nameField.getStyleClass().add("text-field-valid");
			} else {
				removeStyle (nameField, "text-field-valid");
				if (notEmpty (nameField.getText())) {
					nameField.getStyleClass().add("text-field-invalid");
				} else {
					removeStyle (nameField, "text-field-invalid");
				}
			}
		});
		
		titleField.textProperty ().addListener (event -> {
			if (!titleField.getText().equals("")) {
				titleField.getStyleClass ().add ("text-field-valid");
			} else {
				removeStyle (titleField, "text-field-valid");
			}
		});
		
		shortNameField.textProperty ().addListener (event -> {
			if (validShortName(shortNameField.getText())) {
				removeStyle (shortNameField, "text-field-invalid");
				shortNameField.getStyleClass ().add ("text-field-valid");
			} else {
				removeStyle (shortNameField, "text-field-valid");
				if (notEmpty (shortNameField.getText())) {
					shortNameField.getStyleClass ().add ("text-field-invalid");
				} else {
					removeStyle (shortNameField, "text-field-invalid");
				}
			}
		});
		
		startTimeField.textProperty ().addListener (event -> {
			if (validStartOrStop (startTimeField.getText(), endTimeField.getText())) {
				removeStyle (startTimeField, "text-field-invalid");
				removeStyle (endTimeField, "text-field-invalid");
				startTimeField.getStyleClass ().add("text-field-valid");
				endTimeField.getStyleClass ().add("text-field-valid");
			} else {
				removeStyle (startTimeField, "text-field-valid");
				removeStyle (endTimeField, "text-field-valid");
				removeStyle (startTimeField, "text-field-invalid");
				removeStyle (endTimeField, "text-field-invalid");
				
				if (notEmpty (startTimeField.getText()) && notEmpty (endTimeField.getText())) {
					startTimeField.getStyleClass().add("text-field-invalid");
					endTimeField.getStyleClass().add("text-field-invalid");
				}
			}
		});
		
		endTimeField.textProperty ().addListener (event -> {
			if (validStartOrStop (startTimeField.getText(), endTimeField.getText())) {
				removeStyle (startTimeField, "text-field-invalid");
				removeStyle (endTimeField, "text-field-invalid");
				startTimeField.getStyleClass ().add("text-field-valid");
				endTimeField.getStyleClass ().add("text-field-valid");
			} else {
				removeStyle (startTimeField, "text-field-valid");
				removeStyle (endTimeField, "text-field-valid");
				removeStyle (startTimeField, "text-field-invalid");
				removeStyle (endTimeField, "text-field-invalid");
				
				if (notEmpty (startTimeField.getText()) && notEmpty (endTimeField.getText())) {
					startTimeField.getStyleClass().add("text-field-invalid");
					endTimeField.getStyleClass().add("text-field-invalid");
				}
			}
		});
		
		startField.textProperty ().addListener (event -> {
			if (validGrid(startField.getText())) {
				removeStyle (startField, "text-field-invalid");
				startField.getStyleClass().add("text-field-valid");
			} else {
				removeStyle (startField, "text-field-valid");
				if (notEmpty (startField.getText())) {
					startField.getStyleClass().add("text-field-invalid");
				} else {
					removeStyle (startField, "text-field-invalid");
				}
			}
		});
		
		statusField.textProperty ().addListener (event -> {
			if (validStatus (statusField.getText())) {
				removeStyle (startField, "text-field-invalid");
				statusField.getStyleClass ().add("text-field-valid");
			} else {
				removeStyle (statusField, "text-field-valid");
				
				if (notEmpty (statusField.getText())) {
					statusField.getStyleClass ().add("text-field-invalid");
				} else {
					removeStyle (statusField, "text-field-invalid");
				}
			}
		});
		
		statusField.setTooltip (new Tooltip ("Status must be P, O or V!"));
	}
	
	private void setUpConfirmButton () {
		
		confirmButton.setOnMouseEntered (event -> {
			if (checkValidity()) {
				confirmButton.setText ("Confirm?");
				confirmButton.getStyleClass ().add ("buttonHover");
			} else {
				confirmButton.getStyleClass ().add ("button-invalid");
				confirmButton.setText ("Invalid input!");
			}
		});
		
		confirmButton.setOnMouseExited (event -> {
			confirmButton.setText ("Confirm!");
			removeStyle (confirmButton, "button-invalid");
			removeStyle (confirmButton, "buttonConfirmed");
			removeStyle (confirmButton, "buttonHover");
		});
		
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
								statusField.getText (), " ");
				
				confirmButton.setText("Confirmed!");
				confirmButton.getStyleClass ().add("buttonConfirmed");
				
				EntryAdder adder = new EntryAdder (target);
				adder.execute ();
				
				CoreUI.commandLog.put (++CoreUI.nrOfCommands, adder);
				CoreUI.commandPtr = CoreUI.nrOfCommands;
			}
		});
	}
	
	private boolean checkValidity () {
		if (validName(nameField.getText()) && !notEmpty(nameField.getText())) return false;
		if (validArtist(artistField.getText()) && !notEmpty(artistField.getText())) return false;
		if (!notEmpty(titleField.getText())) return false;
		if (!validShortName(shortNameField.getText())) return false;
		if (!validStatus(statusField.getText())) return false;
		
		if (!validGrid(startField.getText()) && !startField.getText().equals("")) return false;
		
		if (!validStartOrStop (startTimeField.getText(), endTimeField.getText()) &&
				notEmpty(startTimeField.getText()) && notEmpty(endTimeField.getText())) return false;
		
		return true;
	}
	
	private boolean notEmpty (String name) {
		return !name.equals("");
	}
	
	private boolean validName (String name) {
		for (ParticipantData pData : CoreUI.inputData.getParticipants ()) {
			if (pData.getName ().equals (name)) return false;
		}
		return !name.equals ("");
	}
	
	private boolean validArtist (String artist) {
		for (ParticipantData pData : CoreUI.inputData.getParticipants ()) {
			if (pData.getArtist ().equals (artist)) return false;
		}
		return !artist.equals ("");
	}
	
	private boolean validShortName (String shortName) {
		boolean isUpper = true;
		
		for (char c : shortName.toCharArray ()) {
			if (!Character.isUpperCase (c)) isUpper = false;
		}
		
		for (ParticipantData pData : CoreUI.inputData.getParticipants ()) {
			if (pData.getShortName ().equals (shortName)) return false;
		}
		
		return isUpper && shortName.length () == 3;
	}
	
	private boolean validStatus (String status) {
		return status.equals ("P") || status.equals("V") || status.equals("O");
	}
	
	private void removeStyle (Control tf, String rStyle) {
		ObservableList<String> styles = FXCollections.observableArrayList();
		
		for (String style : tf.getStyleClass ()) {
			if (style.equals(rStyle)) styles.add(style);
		}
		
		tf.getStyleClass().removeAll(styles);
	}
	
	private boolean validStartOrStop (String start, String stop) {
		return (NumberUtils.isNumber (start) && NumberUtils.isNumber (stop) &&
				Integer.parseInt (start) >= 0 && Integer.parseInt(stop) > 0 &&
				Integer.parseInt (stop) - Integer.parseInt(start) > 10);
	}
	
	private boolean validGrid (String grid) {
		return (NumberUtils.isNumber (grid) && Integer.parseInt (grid) > 0);
	}
}
