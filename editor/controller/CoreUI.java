package controller;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.InputDataModel;
import model.ParticipantData;
import scoreboard.Scoreboard;
import controller.commands.DataSaver;
import controller.commands.EntryCommand;
import controller.commands.EntryRemover;
import controller.commands.EntryWriter;
import controller.commands.HelpDisplayer;
import controller.commands.MacroCommand;
import controller.commands.TableClearer;
import controller.commands.TableLoader;

/*
 * TODO: File Management (*.xsco data?)
 * TODO: Update items of combo boxes depending on votees already picked
 * TODO: Previewer?
 */
public class CoreUI extends Application implements Initializable {
	
	public static int nrOfCommands;
	public static int commandPtr;
	
	@FXML Pane content;
	
	@FXML TableView<ParticipantData> table;
	
	@FXML TableColumn<ParticipantData, String> nationNameCol;
	@FXML TableColumn<ParticipantData, String> shortnameCol;
	@FXML TableColumn<ParticipantData, String> artistCol;
	@FXML TableColumn<ParticipantData, String> titleCol;
	@FXML TableColumn<ParticipantData, Integer> startCol;
	@FXML TableColumn<ParticipantData, Integer> stopCol;
	@FXML TableColumn<ParticipantData, Integer> gridCol;
	@FXML TableColumn<ParticipantData, String> statusCol;
	
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
	
	@FXML
	public TextField editionName;
	@FXML
	public TextField editionNumberField;
	@FXML
	public CheckBox createBannersBox;
	@FXML
	public CheckBox traditionalVotingCheckBox;
	@FXML
	public CheckBox fullScreenBox;
	@FXML
	public CheckBox prettyFlagsBox;
	
	@FXML
	public Slider speedSlider;
	
	public static InputDataModel inputData = new InputDataModel();
	public static HashMap<Integer, EntryCommand> commandLog = new HashMap<>();
	
	VoteRegistrator voteRegistrator = new VoteRegistrator ();
	EntryAdderUI entryAdder = new EntryAdderUI ();
	
	private Stage primaryStage;
	File currentSaveFile;
	
	public static void main (String[] args) {
		launch (args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {	
		this.primaryStage = primaryStage;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation (getClass ().getResource ("../view/CoreUI.fxml"));
		content = (Pane) loader.load ();
		
		Scene scene = new Scene (content);
		
		primaryStage.setResizable (false);
		primaryStage.setScene (scene);
		primaryStage.setTitle ("Participant Editor");
		primaryStage.show ();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
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
			e.printStackTrace ();
		}
	}
	
	private void removeEntry () {
		MacroCommand macroCommand = new MacroCommand (null);
		
		System.out.println("Part of macro: ");
		for (ParticipantData pData : table.getSelectionModel ().getSelectedItems ()) {
			System.out.println(pData.getName());
			EntryRemover remover = new EntryRemover (pData);
			macroCommand.getCommands ().add (remover);
		}
		
		macroCommand.execute ();
		commandLog.put (++nrOfCommands, macroCommand);
		commandPtr = nrOfCommands;
	}
	
	private void setVotes() {
		try {
			voteRegistrator.start (new Stage());
		} catch (Exception e) {
			e.printStackTrace();
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
	}
	
	private void load () {
		TableLoader loader = new TableLoader (this);
		loader.execute ();
	}
	
	private void save (boolean saveAs) {
		DataSaver dataSaver = new DataSaver (new EntryWriter(), currentSaveFile);
		dataSaver.save (saveAs);
	}
	
	private void help () {
		HelpDisplayer helper = new HelpDisplayer ();
		helper.execute ();
	}

	private void setUpTableView () {
		
		CellCentralizer<Integer> intCentralizer = new CellCentralizer<> ();
		
		table.setEditable (true);
		table.getSelectionModel ().setSelectionMode (SelectionMode.MULTIPLE);
		
		nationNameCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("name"));
		shortnameCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("shortName"));
		artistCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("artist"));
		titleCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("title"));
		startCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, Integer>("start"));
		stopCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, Integer>("stop"));
		gridCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, Integer>("grid"));
		statusCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("status"));
		
		nationNameCol.setCellFactory (TextFieldTableCell.<ParticipantData>forTableColumn ());
		shortnameCol.setCellFactory (TextFieldTableCell.<ParticipantData>forTableColumn ());
		artistCol.setCellFactory (TextFieldTableCell.<ParticipantData>forTableColumn ());
		titleCol.setCellFactory (TextFieldTableCell.<ParticipantData>forTableColumn ());
		startCol.setCellFactory (intCentralizer);
		stopCol.setCellFactory (intCentralizer);
		gridCol.setCellFactory (intCentralizer);
		statusCol.setCellFactory (TextFieldTableCell.<ParticipantData>forTableColumn ());
		
		nationNameCol.setStyle ("-fx-alignment: center;");
		shortnameCol.setStyle ("-fx-alignment: center;");
		artistCol.setStyle ("-fx-alignment: center;");
		titleCol.setStyle ("-fx-alignment: center;");
		startCol.setStyle ("-fx-alignment: center;");
		stopCol.setStyle ("-fx-alignment: center;");
		gridCol.setStyle ("-fx-alignment: center;");
		statusCol.setStyle ("-fx-alignment: center;");
		
		nationNameCol.setOnEditCommit (event -> {
			((ParticipantData)(event.getTableView ().getItems ().get (event.getTablePosition ().getRow ()))).setName (event.getNewValue ());
		});

		table.itemsProperty ().bind (inputData.getParticipantsProperty ());
		table.getSortOrder ().add (nationNameCol);
		inputData.getSelectedIndexProperty ().bind (table.getSelectionModel ().selectedIndexProperty ());
	}
	
	private void setUpButtons () {
		flagDirButton.setOnMouseClicked (event -> {
			DirectoryChooser dirChooser = new DirectoryChooser ();
			dirChooser.setInitialDirectory (new File (System.getProperty("user.dir")));
			dirChooser.setTitle ("Folder that contains the flags...");
			
			File selected = dirChooser.showDialog (null);
			
			inputData.setFlagDirectory (selected == null ? null : selected.getAbsolutePath ());
		});
		
		flagDirButton.setOnMouseEntered (event -> {
			flagDirButton.setText ("Add the folder containing the flag images here!");
		});
		
		flagDirButton.setOnMouseExited (event -> {
			flagDirButton.setText ("Flag Directory");
		});
		
		flagDirButton.textProperty ().addListener ((observable, oldValue, newValue) -> {
			if (inputData.getFlagDirectory() != null) {
				String destination = inputData.getFlagDirectory ();
				String[] tokens = destination.split ("\\\\");
				String show = ".../" + tokens[tokens.length-3] + "/" + 
						tokens[tokens.length-2] + "/" + tokens[tokens.length-1];
				flagDirButton.setText ("Flag Directory set to: " + show + " \u2713");
			}
		});
		
		entryDirButton.setOnMouseClicked (event -> {
			DirectoryChooser dirChooser = new DirectoryChooser ();
			dirChooser.setInitialDirectory (new File (System.getProperty("user.dir")));
			dirChooser.setTitle ("Folder that contains the entries...");
			
			File selectedFile = dirChooser.showDialog (null);
			
			inputData.setEntriesDirectory (selectedFile == null ? null : selectedFile.getAbsolutePath ());
		});
		
		entryDirButton.setOnMouseEntered (event -> {
			entryDirButton.setText ("Add the folder containing the entry video files here!");
		});
		
		entryDirButton.setOnMouseExited (event -> {
			entryDirButton.setText ("Entries Directory");
		});
		
		entryDirButton.textProperty ().addListener ((observable, oldValue, newValue) -> {
			if (inputData.getEntriesDirectory() != null) {
				String destination = inputData.getEntriesDirectory ();
				String[] tokens = destination.split ("\\\\");
				String show = ".../" + tokens[tokens.length-2] + "/" + tokens[tokens.length-1];
				entryDirButton.setText ("Entries Directory set to: " + show + " \u2713");
			}
		});
		
		prettyFlagDirButton.setOnMouseClicked (event -> {
			DirectoryChooser dirChooser = new DirectoryChooser ();
			dirChooser.setInitialDirectory (new File (System.getProperty ("user.dir")));
			dirChooser.setTitle ("Folder that contains the pretty flags...");
			
			File selectedFile = dirChooser.showDialog (null);
			
			inputData.setPrettyFlagDirectory (selectedFile == null ? null : selectedFile.getAbsolutePath ());
		});
		
		prettyFlagDirButton.setOnMouseEntered (event -> {
			prettyFlagDirButton.setText ("Add the folder containing the pretty flag images here!");
		});
		
		prettyFlagDirButton.setOnMouseExited (event -> {
			prettyFlagDirButton.setText ("Pretty Flag Directory");
		});
		
		prettyFlagDirButton.textProperty ().addListener ((observable, oldValue, newValue) -> {
			if (inputData.getPrettyFlagDirectory() != null) {
				String destination = inputData.getPrettyFlagDirectory ();
				String[] tokens = destination.split ("\\\\");
				String show = ".../" + tokens[tokens.length-2] + "/" + tokens[tokens.length-1];
				prettyFlagDirButton.setText ("Pretty flag directory set to: " + show + " \u2713");
			}
		});
		
		addEntryButton.setOnAction (event -> addEntry());
		removeEntryButton.setOnAction (event -> removeEntry());
		setVotesButton.setOnAction (event -> setVotes());
		
		startButton.setOnAction (event -> {
			try {	
				Scoreboard sc = new Scoreboard();
				sc.start (new Stage());
			} catch (Exception e) {
				e.printStackTrace();
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
	
	private class CellCentralizer <T> implements Callback<TableColumn<ParticipantData, T>, TableCell<ParticipantData, T>> {

		@Override public TableCell<ParticipantData, T> call (TableColumn<ParticipantData, T> tc) {
			TableCell<ParticipantData, T> cell = new TableCell<ParticipantData, T>() {
				
				@Override public void updateItem (T item, boolean empty) {
					super.updateItem(item, empty);
					setText(empty ? null : getString());
				}

				private String getString() {
					return getItem() == null ? "" : getItem().toString();
				}
			};

            cell.setStyle("-fx-alignment: CENTER;");
            return cell;
		}
	}
}