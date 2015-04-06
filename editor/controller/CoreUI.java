package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.InputDataModel;
import model.ParticipantData;

import org.apache.commons.lang3.math.NumberUtils;

import scoreboard.Scoreboard;
import utilities.Utilities;
import controller.commands.DataSaver;
import controller.commands.EntryCommand;
import controller.commands.EntryEditor;
import controller.commands.EntryRemover;
import controller.commands.EntryWriter;
import controller.commands.HelpDisplayer;
import controller.commands.MacroCommand;
import controller.commands.TableClearer;
import controller.commands.TableLoader;

public class CoreUI extends Application implements Initializable {
	
	public static int nrOfCommands;
	public static int commandPtr;
	
	@FXML Pane content;
	
	@FXML TableView<ParticipantData> table;
	
	@FXML TableColumn<ParticipantData, String> nationNameCol;
	@FXML TableColumn<ParticipantData, String> shortnameCol;
	@FXML TableColumn<ParticipantData, String> artistCol;
	@FXML TableColumn<ParticipantData, String> titleCol;
	@FXML TableColumn<ParticipantData, String> startCol;
	@FXML TableColumn<ParticipantData, String> stopCol;
	@FXML TableColumn<ParticipantData, String> gridCol;
	@FXML TableColumn<ParticipantData, String> statusCol;
	@FXML TableColumn<ParticipantData, String> voteNrCol;
	
	@FXML Button addEntryButton;
	@FXML Button removeEntryButton;
	@FXML Button setVotesButton;
	@FXML Button flagDirButton;
	@FXML Button entryDirButton;
	@FXML Button prettyFlagDirButton;
	
	@FXML Button startButton;
	
	@FXML MenuItem loadMenuItem;
	@FXML MenuItem saveMenuItem;
	@FXML MenuItem saveAsMI;
	@FXML MenuItem addP_MI;
	@FXML MenuItem removeP_MI;
	@FXML MenuItem setVotesMI;
	@FXML MenuItem undoMI;
	@FXML MenuItem redoMI;
	@FXML MenuItem clearMI;
	@FXML MenuItem closeMI;
	@FXML MenuItem aboutMI;
	
	@FXML public TextField editionName;
	@FXML public TextField editionNumberField;
	@FXML public CheckBox createBannersBox;
	@FXML public CheckBox traditionalVotingCheckBox;
	@FXML public CheckBox fullScreenBox;
	@FXML public CheckBox prettyFlagsBox;
	
	@FXML public Slider speedSlider;
	
	public static InputDataModel inputData = new InputDataModel();
	public static HashMap<Integer, EntryCommand> commandLog = new HashMap<>();
	
	Image confirmBG;
	Image warningBG;
	Image errorBG;
	Image exceptionBG;
	
	VoteRegistrator voteRegistrator = new VoteRegistrator ();
	EntryAdderUI entryAdder = new EntryAdderUI ();
	
	private Stage primaryStage;
	File currentSaveFile;
	Scene scene;
	
	enum PopUpMessage { CONFIRMATION, WARNING, ERROR, EXCEPTION }
	
	public static void main (String[] args) { launch (args); }

	@Override
	public void start(Stage primaryStage) throws Exception {			
		this.primaryStage = primaryStage;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation (getClass ().getResource ("/view/CoreUI.fxml"));
		content = (Pane) loader.load ();
		
		double originalHeight = content.getHeight ();
		double originalWidth = content.getWidth ();
		
		if (Pane.USE_COMPUTED_SIZE == content.getPrefHeight ()) {
			content.setPrefHeight (originalHeight);
		} else originalHeight = content.getPrefHeight ();
		
		if (Pane.USE_COMPUTED_SIZE == content.getPrefWidth ()) {
			content.setPrefWidth (originalWidth);
		} else originalWidth = content.getPrefWidth ();
		
		Group wrapper = new Group (content);
		StackPane sPane = new StackPane ();
		sPane.getChildren ().add (wrapper);
		
		Scene scene = new Scene (sPane);
		scene.getStylesheets ().add ("/view/CoreUI.css");
		this.scene = scene;
	
		wrapper.scaleXProperty ().bind (scene.widthProperty ().divide (originalWidth));
		wrapper.scaleYProperty ().bind (scene.heightProperty ().divide (originalHeight));
		
		primaryStage.setResizable (true);
		primaryStage.setScene (scene);
		primaryStage.setTitle ("Participant Editor");
		primaryStage.getIcons ().add (Utilities.readImage ("resources/Icon.png"));
		primaryStage.show ();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		setUpPopUps ();
		setUpTableView ();
		setUpButtons ();
		setUpMenu ();
		setUpTextFields ();
		setUpCheckBoxes ();
		setUpSlider ();
	}
	
	private void addEntry () {
		try { 
			entryAdder.init (new Stage()); 
		} catch (Exception e) { 
			PopUp popUp = new PopUp (PopUpMessage.EXCEPTION, this, "Error adding a participant...");
			popUp.show();
		}
	}
	
	private void removeEntry () {
		MacroCommand macroCommand = new MacroCommand (null);
		StringBuilder sb = new StringBuilder ("Goodbye: ");
		
		for (ParticipantData pData : table.getSelectionModel ().getSelectedItems ()) {
			EntryRemover remover = new EntryRemover (pData);
			macroCommand.getCommands ().add (remover);
			sb.append (pData.getShortName () + " ");
		}
		
		if (macroCommand.getCommands ().size () == 1) {
			String name = table.getSelectionModel ().getSelectedItems ().get (0).getName ();
			String shortName = table.getSelectionModel ().getSelectedItems ().get (0).getShortName ();
			sb.delete (0, sb.length () - 1);
			sb.append ("Goodbye... " + (name.length() > 15 ? shortName : name));
		} else if (macroCommand.getCommands ().size () > 4) {
			sb.delete (0, sb.length () - 1);
			sb.append ("Goodbye...: " + macroCommand.getCommands ().size () + " participants.");
		}
		
		macroCommand.execute ();
		commandLog.put (++nrOfCommands, macroCommand);
		commandPtr = nrOfCommands;
		
		PopUp popUp = new PopUp (PopUpMessage.CONFIRMATION, this, sb.toString ());
		popUp.show ();
	}
	
	private void setVotes() {
		if (table.getSelectionModel ().getSelectedItems ().size () == 1) {
			try { 
				voteRegistrator.start (new Stage()); 
			} catch (Exception e) { 
				visualExceptionMsg ("Error setting votes");
			} 
		} else {
			String message = table.getSelectionModel().getSelectedItems ().size () + " participants selected!";
			PopUp popUp = new PopUp (PopUpMessage.ERROR, this, message);
			popUp.show ();
		}
	}
	
	private void undo () {
		if (commandPtr < 1) return;
		else commandLog.get (commandPtr).undo ();
	}
	
	private void redo() {
		commandPtr++;
		
		if (commandPtr > commandLog.size ()) return;
		else commandLog.get (commandPtr).execute ();
	}
	
	private void clear () {
		TableClearer clearer = new TableClearer ();
		clearer.execute ();
		
		String message = "All has been removed.";
		PopUp popUp = new PopUp (PopUpMessage.CONFIRMATION, this, message);
		popUp.show ();
	}
	
	private void load () {
		TableLoader loader = new TableLoader (this, new TableClearer ());
		
		try { loader.execute (); } 
		catch (IOException ioex) { ioex.printStackTrace(); }
	}
	
	private void save (boolean saveAs) {
		DataSaver dataSaver = new DataSaver (new EntryWriter(), currentSaveFile);
		
		try {
			dataSaver.save (saveAs);
			
			String message = inputData.getNameOfEdition () + " " + inputData.getEditionNr () + " saved!";
			PopUp popUp = new PopUp (PopUpMessage.CONFIRMATION, this, message);
			popUp.show ();
		} catch (IOException e) { 
			visualExceptionMsg ("Error while saving!");
		}
	}
	
	private void voteOverview () {
		System.out.println ("These have voted:");
		
		Set<ParticipantData> nonVoters = new HashSet<> (inputData.getParticipants ());

		for (Map.Entry<ParticipantData, ?> mapE : inputData.getVotes ().entrySet ()) {
			System.out.println (mapE.getKey ().getName ());
			nonVoters.remove (mapE.getKey ());
		}
		
		System.out.println ("These are missing...: ");
		
		for (ParticipantData pData : nonVoters) {
			if (!inputData.getVotes ().entrySet ().contains (pData)) {
				System.out.println (pData.getName ());
			}
		}
	}
	
	private void help () {
		HelpDisplayer helper = new HelpDisplayer ();
		helper.execute ();
	}
	
	private void setUpPopUps () {
		try {
			confirmBG = Utilities.readImage ("resources/Graphics/EditorBackgrounds/Confirmation.png");
			warningBG = Utilities.readImage ("resources/Graphics/EditorBackgrounds/Warning.png");
			errorBG = Utilities.readImage ("resources/Graphics/EditorBackgrounds/Error.png");
			exceptionBG = Utilities.readImage ("resources/Graphics/EditorBackgrounds/Exception.png");
		} catch (Exception e) {}
	}

	@SuppressWarnings("unchecked")
	private void setUpTableView () {		
		table.setEditable (true);
		table.getSortOrder ().add (nationNameCol);
		table.getSelectionModel ().setSelectionMode (SelectionMode.MULTIPLE);
		
		nationNameCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("name"));
		shortnameCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("shortName"));
		artistCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("artist"));
		titleCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("title"));
		startCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String>("start"));
		stopCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String>("stop"));
		gridCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String>("grid"));
		statusCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("status"));
		voteNrCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("voteNr"));
		
		int counter = 0;
		
		for (TableColumn<ParticipantData, ?> column : table.getColumns()) {
			TableColumn<ParticipantData, String> cColumn = (TableColumn<ParticipantData, String>) column;
			cColumn.setCellFactory (TextFieldTableCell.<ParticipantData>forTableColumn ());
			cColumn.getStyleClass ().add ((counter ++) % 2 == 0 ? "evenColumn" : "oddColumn");
		}
		
		table.setRowFactory (tv -> {
			TableRow<ParticipantData> pRow = new TableRow<> ();
			pRow.setOnMouseEntered (event -> {
				pRow.getStyleClass ().add ("selectedRow");
			});
			pRow.setOnMouseExited (event -> {
				pRow.getStyleClass ().remove ("selectedRow");
			});
			return pRow;
		});
		
		nationNameCol.setOnEditCommit (event -> {			
			if (checkNoDuplicateName(event.getNewValue())) {
				EntryEditor eEditor = new EntryEditor ((ParticipantData)(event.getTableView ().getItems ().get (event.getTablePosition ().getRow ())), 
						"name", event.getOldValue (), event.getNewValue ());
				eEditor.execute ();
				commandLog.put (++nrOfCommands, eEditor);
				commandPtr = nrOfCommands;
			} else {
				String message = "Nope! Duplicate nation names!";
				PopUp popUp = new PopUp (PopUpMessage.ERROR, this, message);
				popUp.show ();
			}
		});
		
		shortnameCol.setOnEditCommit (event -> {
			String newCode = event.getNewValue ();

			if (newCode.length () == 3 && checkIfUpperCase (newCode) && checkNoDuplicateShortName(newCode)) {
				EntryEditor eEditor = new EntryEditor ((ParticipantData)(event.getTableView ().getItems ().get (event.getTablePosition ().getRow ())), 
						"shortName", event.getOldValue (), event.getNewValue ());
				eEditor.execute ();
				commandLog.put (++nrOfCommands, eEditor);
				commandPtr = nrOfCommands;
			} else if (newCode.length () != 3){
				String message = "Short code must have three letters!";
				PopUp popUp = new PopUp (PopUpMessage.ERROR, this, message);
				popUp.show ();
			} else if (!checkIfUpperCase (newCode)) {
				String message = "Short code must be uppercase!";
				PopUp popUp = new PopUp (PopUpMessage.ERROR, this, message);
				popUp.show ();
			} else {
				String message = "Nope! Duplicate short codes!";
				PopUp popUp = new PopUp (PopUpMessage.ERROR, this, message);
				popUp.show ();
			}
		});
		
		artistCol.setOnEditCommit (event -> {	
			if (checkNoDuplicateArtist(event.getNewValue())) {
				EntryEditor eEditor = new EntryEditor ((ParticipantData)(event.getTableView ().getItems ().get (event.getTablePosition ().getRow ())), 
						"artist", event.getOldValue (), event.getNewValue ());
				eEditor.execute ();
				commandLog.put (++nrOfCommands, eEditor);
				commandPtr = nrOfCommands;
			} else {
				String message = "Nope! " + System.lineSeparator () + "Duplicate artist names!";
				PopUp popUp = new PopUp (PopUpMessage.ERROR, this, message);
				popUp.show ();
			}
		});
		
		titleCol.setOnEditCommit (event -> {
			EntryEditor eEditor = new EntryEditor ((ParticipantData)(event.getTableView ().getItems ().get (event.getTablePosition ().getRow ())), 
					"title", event.getOldValue (), event.getNewValue ());
			eEditor.execute ();
			commandLog.put (++nrOfCommands, eEditor);
			commandPtr = nrOfCommands;
		});
		
		startCol.setOnEditCommit (event -> {
			if (NumberUtils.isNumber (event.getNewValue ()) && Integer.parseInt (event.getNewValue ()) >= 0) {
				EntryEditor eEditor = new EntryEditor ((ParticipantData)(event.getTableView ().getItems ().get (event.getTablePosition ().getRow ())), 
						"start", event.getOldValue (), event.getNewValue ());
				eEditor.execute ();
				commandLog.put (++nrOfCommands, eEditor);
				commandPtr = nrOfCommands;
			} else {
				String message = "Invalid!";
				PopUp popUp = new PopUp (PopUpMessage.ERROR, this, message);
				popUp.show ();
			}
		});
		
		stopCol.setOnEditCommit (event -> {
			int number;
			
			if (NumberUtils.isNumber (event.getNewValue ()) && (number = Integer.parseInt (event.getNewValue ())) > 0) {

				int difference = number - Integer.parseInt(inputData.getSelectedParticipant().getStart());
				
				if (difference >= 5 && difference <= 10) {
					String message = difference + " seconds is too short!";
					PopUp popUp = new PopUp (PopUpMessage.WARNING, this, message);
					popUp.show ();
					return;
				} else if (difference < 5) {
					String message = "Invalid segment length!";
					PopUp popUp = new PopUp (PopUpMessage.ERROR, this, message);
					popUp.show ();
					return;
				}
				
				EntryEditor eEditor = new EntryEditor ((ParticipantData)(event.getTableView ().getItems ().get (event.getTablePosition ().getRow ())), 
						"stop", event.getOldValue (), event.getNewValue ());
				eEditor.execute ();
				commandLog.put (++nrOfCommands, eEditor);
				commandPtr = nrOfCommands;
			}
		});
		
		gridCol.setOnEditCommit (event -> {
			if (NumberUtils.isNumber (event.getNewValue ()) && Integer.parseInt (event.getNewValue ()) > 0 && checkNoDuplicateGrid (event.getNewValue())) {
				EntryEditor eEditor = new EntryEditor ((ParticipantData)(event.getTableView ().getItems ().get (event.getTablePosition ().getRow ())), 
						"grid", event.getOldValue (), event.getNewValue ());
				eEditor.execute ();
				commandLog.put (++nrOfCommands, eEditor);
				commandPtr = nrOfCommands;
			} else {
				String message = "Invalid!";
				PopUp popUp = new PopUp (PopUpMessage.ERROR, this, message);
				popUp.show ();
			}
		});
		
		statusCol.setOnEditCommit (event -> {
			if (event.getNewValue().equals("P") || event.getNewValue().equals("O") || event.getNewValue ().equals ("V")) {
				EntryEditor eEditor = new EntryEditor ((ParticipantData)(event.getTableView ().getItems ().get (event.getTablePosition ().getRow ())), 
						"status", event.getOldValue (), event.getNewValue ());
				eEditor.execute ();
				commandLog.put (++nrOfCommands, eEditor);
				commandPtr = nrOfCommands;			
			} else {
				String message = "Status must be P, O or V!";
				PopUp popUp = new PopUp (PopUpMessage.ERROR, this, message);
				popUp.show ();
			}
		});
		
		voteNrCol.setOnEditCommit (event -> {
			
			boolean empty = event.getNewValue().equals ("") || event.getNewValue ().equals (" ");
			boolean valid = empty || (NumberUtils.isNumber (event.getNewValue()) && Integer.parseInt (event.getNewValue ()) > 0);
			
			if (empty || (valid && checkNoDuplicateVoteNr (event.getNewValue ()))) {
				EntryEditor eEditor = new EntryEditor ((ParticipantData)(event.getTableView ().getItems ().get (event.getTablePosition ().getRow ())), 
						"voteNr", event.getOldValue (), event.getNewValue ());
				eEditor.execute ();
				commandLog.put (++nrOfCommands, eEditor);
				commandPtr = nrOfCommands;
			} else {
				String message = "Invalid!";
				PopUp popUp = new PopUp (PopUpMessage.ERROR, this, message);
				popUp.show ();
			}
		});

		table.itemsProperty ().bind (inputData.getParticipantsProperty ());
		table.getSortOrder ().add (nationNameCol);
		inputData.getSelectedIndexProperty ().bind (table.getSelectionModel ().selectedIndexProperty ());
	}
	
	private void setUpButtons () {
		flagDirButton.setOnMouseClicked (event -> {
			DirectoryChooser dirChooser = new DirectoryChooser ();
			dirChooser.setInitialDirectory (new File ("."));
			dirChooser.setTitle ("Folder that contains the flags...");
			
			File selected = dirChooser.showDialog (null);
			
			inputData.setFlagDirectory (selected == null ? null : selected.getAbsolutePath ().replace ("\\", "/"));
		});
		
		flagDirButton.setOnMouseEntered (event -> {
			flagDirButton.setText ("Add the folder containing the flag images here!");
		});
		
		flagDirButton.setOnMouseExited (event -> {
			flagDirButton.setText ("Flag Directory");
		});
		
		flagDirButton.textProperty ().addListener ((observable, oldValue, newValue) -> {
			if (inputData.getFlagDirectory() != null && (!inputData.getFlagDirectory().equals("null"))) {
				String destination = inputData.getFlagDirectory ();
				String[] tokens = destination.split ("/");
				String show = ".../" + tokens[tokens.length-2] + "/" + tokens[tokens.length-1];
				flagDirButton.setText ("Flag Directory set to: " + show + " \u2713");
			}
		});
		
		entryDirButton.setOnMouseClicked (event -> {
			DirectoryChooser dirChooser = new DirectoryChooser ();
			dirChooser.setInitialDirectory (new File ("."));
			dirChooser.setTitle ("Folder that contains the entries...");
			
			File selectedFile = dirChooser.showDialog (null);
			
			inputData.setEntriesDirectory (selectedFile == null ? null : selectedFile.getAbsolutePath ().replace ("\\", "/"));
		});
		
		entryDirButton.setOnMouseEntered (event -> {
			entryDirButton.setText ("Add the folder containing the entry video files here!");
		});
		
		entryDirButton.setOnMouseExited (event -> {
			entryDirButton.setText ("Entries Directory");
		});
		
		entryDirButton.textProperty ().addListener ((observable, oldValue, newValue) -> {
			if (inputData.getEntriesDirectory() != null && (!inputData.getEntriesDirectory().equals("null"))) {
				String destination = inputData.getEntriesDirectory ();
				String[] tokens = destination.split ("/");
				String show = ".../" + tokens[tokens.length-2] + "/" + tokens[tokens.length-1];
				entryDirButton.setText ("Entries Directory set to: " + show + " \u2713");
			}
		});
		
		prettyFlagDirButton.setOnMouseClicked (event -> {
			DirectoryChooser dirChooser = new DirectoryChooser ();
			dirChooser.setInitialDirectory (new File ("."));
			dirChooser.setTitle ("Folder that contains the pretty flags...");
			
			File selectedFile = dirChooser.showDialog (null);
			
			inputData.setPrettyFlagDirectory (selectedFile == null ? null : selectedFile.getAbsolutePath ().replace ("\\", "/"));
		});
		
		prettyFlagDirButton.setOnMouseEntered (event -> {
			prettyFlagDirButton.setText ("Add the folder containing the pretty flag images here!");
		});
		
		prettyFlagDirButton.setOnMouseExited (event -> {
			prettyFlagDirButton.setText ("Pretty Flag Directory");
		});
		
		prettyFlagDirButton.textProperty ().addListener ((observable, oldValue, newValue) -> {
			if (inputData.getPrettyFlagDirectory() != null && (!inputData.getPrettyFlagDirectory().equals("null"))) {
				String destination = inputData.getPrettyFlagDirectory ();
				String[] tokens = destination.split ("/");
				String show = ".../" + tokens[tokens.length-2] + "/" + tokens[tokens.length-1];
				prettyFlagDirButton.setText ("Pretty flag directory set to: " + show + " \u2713");
			}
		});
		
		addEntryButton.setOnAction (event -> addEntry());
		removeEntryButton.setOnAction (event -> removeEntry());
		setVotesButton.setOnMouseClicked (event -> {
			if (event.isControlDown()) voteOverview ();
			else setVotes();
		});
	
		removeEntryButton.setOnMouseEntered (event -> {
			int selectedNr = table.getSelectionModel().getSelectedItems().size();
			
			if (selectedNr == 0) return;
			
			if (table.getSelectionModel().getSelectedItems().size() == 1) {
				int nameLength = table.getSelectionModel().getSelectedItems().get(0).getName().length();
				
				if (nameLength < 10) removeEntryButton.setText ("Remove " + inputData.getSelectedParticipant() + "?");
				else if (nameLength < 20) {
					removeEntryButton.setStyle ("-fx-font-size: 11.0pt;");
					removeEntryButton.setText ("Remove " + inputData.getSelectedParticipant() + "?");
				} else removeEntryButton.setText ("Remove " + inputData.getSelectedParticipant().getShortName() + "?");	
			} else if (table.getSelectionModel().getSelectedItems().size() < 4) {
				StringBuffer sb = new StringBuffer ("Remove ");
				
				for (ParticipantData pData : table.getSelectionModel ().getSelectedItems ()) {
					sb.append (pData.getShortName() + " ");
				}
				removeEntryButton.setText (sb + "?");
			} else {
				removeEntryButton.setText ("Remove " + table.getSelectionModel ().getSelectedItems ().size () + " participants?");
			}
		});
		
		setVotesButton.setOnMouseEntered (event -> {
			if (event.isControlDown()) {
				setVotesButton.setText ("Who voted? Overview");
				return;
			}
			if (table.getSelectionModel().getSelectedItems().size() == 1) {
				int nameLength = table.getSelectionModel().getSelectedItems().get(0).getName().length();
				
				if (nameLength < 10) setVotesButton.setText ("Set votes for: " + inputData.getSelectedParticipant());
				else if (nameLength < 17) {
					setVotesButton.setStyle ("-fx-font-size: 10.0pt;");
					setVotesButton.setText ("Set votes for: " + inputData.getSelectedParticipant());
				} else setVotesButton.setText ("Set votes for: " + inputData.getSelectedParticipant().getShortName());	
			}
		});
		
		removeEntryButton.setOnMouseExited (event -> {
			removeEntryButton.setStyle ("-fx-font-size: 14.0pt;");
			removeEntryButton.setText ("Remove Entry");
		});
		
		setVotesButton.setOnMouseExited (event -> {
			setVotesButton.setStyle ("-fx-font-size: 14.0pt;");
			setVotesButton.setText ("Set Votes");
		});
		
		startButton.setOnAction (event -> {
			try {	
				Scoreboard sc = new Scoreboard();
				sc.start (new Stage());
			} catch (Exception e) { 
				visualExceptionMsg ("Yikes! That shouldn't have happened..." + System.lineSeparator () + "Sorry. :( Try again.");
			} catch (OutOfMemoryError oomerr) {
				visualExceptionMsg ("That was too fast for me. :(" + System.lineSeparator () + "Try again in a second!");
			}
		});
	}
	
	private void setUpMenu () {
		loadMenuItem.setOnAction (event -> load());
		saveMenuItem.setOnAction (event -> save(false));
		saveAsMI.setOnAction (event -> save(true));
		undoMI.setOnAction (event -> undo());
		redoMI.setOnAction (event -> redo());
		addP_MI.setOnAction (event -> addEntry());
		removeP_MI.setOnAction (event -> removeEntry ());
		setVotesMI.setOnAction (event -> setVotes());
		clearMI.setOnAction (event -> clear());
		closeMI.setOnAction (event -> primaryStage.hide ());
		aboutMI.setOnAction (event -> help());
	}
	
	private void setUpTextFields () {
		inputData.getNameOfEditionProperty ().bind (editionName.textProperty ());
		inputData.getEditionNrProperty ().bind (editionNumberField.textProperty ());
	}
	
	private void setUpCheckBoxes () {
		inputData.getBannerCreatorActivatedProperty ().bind (createBannersBox.selectedProperty ());
		inputData.getUseFullScreenProperty ().bind (fullScreenBox.selectedProperty ());
		inputData.getUsePrettyFlagsProperty ().bind (prettyFlagsBox.selectedProperty ());
		inputData.getTraditionalVotingProperty ().bind (traditionalVotingCheckBox.selectedProperty ());
	}
	
	private void setUpSlider () {
		inputData.getShowSpeedProperty ().bind (speedSlider.valueProperty ());
		
        speedSlider.setMajorTickUnit(25);
        speedSlider.setSnapToTicks(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);
		speedSlider.setLabelFormatter (new StringConverter<Double>() {
			
			@Override
			public String toString(Double n) {
				if (n < 25.0) {
					return "very slow";
				} else if (n < 50.0) {
					return "slow";
				} else if (n < 75.0) {
					return "normal";
				} else if (n < 100.0) {
					return "fast";
				} else return "very fast";
			}
			
			@Override
			public Double fromString(String string) {
				return speedSlider.getValue ();
			}
		});
	}
	
	private boolean checkNoDuplicateName (String newValue) {
		for (ParticipantData pData : inputData.getParticipants ()) {
			if (pData.getName ().equals (newValue)) return false;
		}
		return true;
	}
	
	private boolean checkNoDuplicateShortName (String newValue) {
		for (ParticipantData pData : inputData.getParticipants ()) {
			if (pData.getShortName ().equals (newValue)) return false;
		}
		return true;
	}
	
	private boolean checkNoDuplicateArtist (String newValue) {
		for (ParticipantData pData : inputData.getParticipants ()) {
			if (pData.getArtist ().equals (newValue)) return false;
		}
		return true;
	}
	
	private boolean checkNoDuplicateGrid (String newValue) {
		for (ParticipantData pData : inputData.getParticipants ()) {
			if (pData.getGrid ().equals(newValue)) return false;
		}
		return true;
	}
	
	private boolean checkIfUpperCase (String newCode) {
		for (char c : newCode.toCharArray ()) {
			if (!Character.isUpperCase (c)) return false;
		}
		return true;
	}
	
	private boolean checkNoDuplicateVoteNr (String voteNr) {
		for (ParticipantData pData : inputData.getParticipants ()) {
			if (pData.getVoteNr ().equals (voteNr)) return false;
		}
		return true;
	}
	
	private void visualExceptionMsg (String message) {
		PopUp popUp = new PopUp (PopUpMessage.EXCEPTION, this, message);
		popUp.show ();
	}
	
	public Image getPopUpBackground (PopUpMessage style) {
		switch (style) {
			case CONFIRMATION: return confirmBG;
			case WARNING: return warningBG;
			case ERROR: return errorBG;
			case EXCEPTION: return exceptionBG;
			default: return null;
		}
	}
}