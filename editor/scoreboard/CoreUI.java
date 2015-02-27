package scoreboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class CoreUI extends Application implements Initializable {
	
	Stage primaryStage;
	
	@FXML Pane content;
	
	@FXML TableView<ParticipantSave> table;
	
	@FXML TableColumn<ParticipantSave, String> nationNameCol;
	@FXML TableColumn<ParticipantSave, String> shortnameCol;
	@FXML TableColumn<ParticipantSave, String> artistCol;
	@FXML TableColumn<ParticipantSave, String> titleCol;
	@FXML TableColumn<ParticipantSave, Integer> startCol;
	@FXML TableColumn<ParticipantSave, Integer> stopCol;
	@FXML TableColumn<ParticipantSave, Integer> gridCol;
	@FXML TableColumn<ParticipantSave, String> statusCol;
	
	@FXML Button addEntryButton;
	@FXML Button setVotesButton;
	@FXML Button flagDirButton;
	@FXML Button entryDirButton;
	@FXML Button prettyFlagDirButton;
	
	private final ObservableList<ParticipantSave> entries = FXCollections.observableArrayList (
			new ParticipantSave ("Ugaly", "UGA", "Ugaly", "Ugaly", 42, 65, 3, "F"));
	
	public static void main(String[] args) {
		launch (args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation (getClass ().getResource ("Dialog.fxml"));
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
		
	}

	private void setUpTableView () {
		
		CellCentralizer<Integer> intCentralizer = new CellCentralizer<> ();
		CellCentralizer<String> textCentralizer = new CellCentralizer<> ();
		
		nationNameCol.setCellValueFactory (new PropertyValueFactory<ParticipantSave, String> ("name"));
		shortnameCol.setCellValueFactory (new PropertyValueFactory<ParticipantSave, String> ("shortName"));
		artistCol.setCellValueFactory (new PropertyValueFactory<ParticipantSave, String> ("artist"));
		titleCol.setCellValueFactory (new PropertyValueFactory<ParticipantSave, String> ("title"));
		startCol.setCellValueFactory (new PropertyValueFactory<ParticipantSave, Integer>("start"));
		stopCol.setCellValueFactory (new PropertyValueFactory<ParticipantSave, Integer>("stop"));
		gridCol.setCellValueFactory (new PropertyValueFactory<ParticipantSave, Integer>("grid"));
		statusCol.setCellValueFactory (new PropertyValueFactory<ParticipantSave, String> ("status"));
		
		nationNameCol.setCellFactory (textCentralizer);
		shortnameCol.setCellFactory (textCentralizer);
		artistCol.setCellFactory (textCentralizer);
		titleCol.setCellFactory (textCentralizer);
		startCol.setCellFactory (intCentralizer);
		stopCol.setCellFactory (intCentralizer);
		gridCol.setCellFactory (intCentralizer);
		statusCol.setCellFactory (textCentralizer);
		
		table.getItems ().setAll (entries);
	}
	
	private void setUpButtons () {
		DirChooser dirChooser = new DirChooser();
		
		flagDirButton.setOnMouseClicked (dirChooser);
		entryDirButton.setOnMouseClicked (dirChooser);
		prettyFlagDirButton.setOnMouseClicked (dirChooser);
		
		setVotesButton.setOnAction (new VoteRegistrator ());
	}

	@SuppressWarnings("unused") 
	private ObservableList<ParticipantSave> readConfigFile () throws IOException {
		
		String line;
		String location = System.getProperty ("user.dir") + "/...";
		ObservableList<ParticipantSave> list = FXCollections.observableArrayList ();
		
		BufferedReader reader = new BufferedReader (new FileReader (new File (location)));
		while ((line = reader.readLine ()) != null) {
			String[] tokens = line.split ("\\$");
				
			list.add (new ParticipantSave (tokens[0], tokens[1], tokens[2], tokens[3],
					Integer.parseInt (tokens[4]), Integer.parseInt (tokens[5]),
					Integer.parseInt (tokens[6]), tokens[8]));
		}
			
		reader.close ();
		
		return list;
	}
	
	private class DirChooser implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			DirectoryChooser dirChooser = new DirectoryChooser ();
			dirChooser.setInitialDirectory (new File (System.getProperty("user.dir")));
			dirChooser.setTitle ("Folder that contains the flags...");
			
			File selected = dirChooser.showDialog (null);
			
			System.out.println ("You've selected: " + selected.getAbsolutePath ());
		}
		
	}
	
	private class CellCentralizer <T> implements Callback<TableColumn<ParticipantSave, T>, TableCell<ParticipantSave, T>> {

		@Override public TableCell<ParticipantSave, T> call (TableColumn<ParticipantSave, T> tc) {
			TableCell<ParticipantSave, T> cell = new TableCell<ParticipantSave, T>() {
				
				@Override public void updateItem (T item, boolean empty) {
					super.updateItem(item, empty);
					setText(empty ? null : getString());
					setGraphic(null);
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