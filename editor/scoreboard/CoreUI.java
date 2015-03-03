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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Participant;
import model.ParticipantModel;

public class CoreUI extends Application implements Initializable {
	
	@FXML Pane content;
	
	@FXML TableView<Participant> table;
	
	@FXML TableColumn<Participant, String> nationNameCol;
	@FXML TableColumn<Participant, String> shortnameCol;
	@FXML TableColumn<Participant, String> artistCol;
	@FXML TableColumn<Participant, String> titleCol;
	@FXML TableColumn<Participant, Integer> startCol;
	@FXML TableColumn<Participant, Integer> stopCol;
	@FXML TableColumn<Participant, Integer> gridCol;
	@FXML TableColumn<Participant, String> statusCol;
	
	@FXML Button addEntryButton;
	@FXML Button setVotesButton;
	@FXML Button flagDirButton;
	@FXML Button entryDirButton;
	@FXML Button prettyFlagDirButton;
	
	static ParticipantModel pModel = new ParticipantModel();
	
	public static void main(String[] args) {
		launch (args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {	
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation (getClass ().getResource ("CoreUI.fxml"));
		content = (Pane) loader.load ();
		
		Scene scene = new Scene (content);
		
		primaryStage.setResizable (false);
		primaryStage.setScene (scene);
		primaryStage.setTitle ("Participant Editor");
		primaryStage.show ();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		pModel.addParticipant (new Participant ("Slovenia", "SLO", "Maraaya", "Here For You", 20, 40, 1, "F"));
		pModel.addParticipant (new Participant ("Latvia", "LAT", "Aminata", "Love Injected", 50, 68, 2, "F"));
		pModel.addParticipant (new Participant ("Estonia", "EST", "Elina Born & Stig Rästa", 
				"Goodbye To Yesterday", 50, 68, 3, "F"));
		pModel.addParticipant (new Participant ("Finland", "FIN", "Pertti Kurikan Nimipäivät", 
				"Aina Mun Pitää", 50, 68, 4, "F"));
		pModel.addParticipant (new Participant ("Ireland", "IRL", "Molly Sterling", 
				"Playing With Numbers", 50, 68, 5, "F"));
		pModel.addParticipant (new Participant ("Serbia", "SRB", "Bojana Stamenov", 
				"Ceo Svet Je Moj", 50, 68, 6, "F"));
		pModel.addParticipant (new Participant ("France", "FRA", "Lisa Angell", 
				"N'oubliez pas", 50, 68, 7, "F"));
		pModel.addParticipant (new Participant ("Iceland", "ISL", "María Ólafsdóttir", 
				"Unbroken", 50, 68, 8, "F"));
		pModel.addParticipant (new Participant ("Hungary", "HUN", "Boggie", 
				"Wars For Nothing", 50, 68, 9, "F"));
		pModel.addParticipant (new Participant ("Georgia", "GEO", "Nina Sublati", 
				"Warrior", 50, 68, 10, "F"));
		pModel.addParticipant (new Participant ("Lithuania", "LTU", "Vaidas & Monika", 
				"This Time", 50, 68, 11, "F"));
		pModel.addParticipant (new Participant ("F.Y.R. Macedonia", "MKD", "Daniel Kajmakoski", 
				"Lisja Esenski", 50, 68, 12, "F"));
		
		setUpTableView ();
		setUpButtons ();
		
	}

	private void setUpTableView () {
		
		CellCentralizer<Integer> intCentralizer = new CellCentralizer<> ();
		CellCentralizer<String> textCentralizer = new CellCentralizer<> ();
		
		nationNameCol.setCellValueFactory (new PropertyValueFactory<Participant, String> ("name"));
		shortnameCol.setCellValueFactory (new PropertyValueFactory<Participant, String> ("shortName"));
		artistCol.setCellValueFactory (new PropertyValueFactory<Participant, String> ("artist"));
		titleCol.setCellValueFactory (new PropertyValueFactory<Participant, String> ("title"));
		startCol.setCellValueFactory (new PropertyValueFactory<Participant, Integer>("start"));
		stopCol.setCellValueFactory (new PropertyValueFactory<Participant, Integer>("stop"));
		gridCol.setCellValueFactory (new PropertyValueFactory<Participant, Integer>("grid"));
		statusCol.setCellValueFactory (new PropertyValueFactory<Participant, String> ("status"));
		
		nationNameCol.setCellFactory (textCentralizer);
		shortnameCol.setCellFactory (textCentralizer);
		artistCol.setCellFactory (textCentralizer);
		titleCol.setCellFactory (textCentralizer);
		startCol.setCellFactory (intCentralizer);
		stopCol.setCellFactory (intCentralizer);
		gridCol.setCellFactory (intCentralizer);
		statusCol.setCellFactory (textCentralizer);

		table.itemsProperty ().bind (pModel.getParticipantsProperty ());
		pModel.getSelectedIndexProperty ().bind (table.getSelectionModel ().selectedIndexProperty ());
	}
	
	private void setUpButtons () {
		DirChooser dirChooser = new DirChooser();
		
		flagDirButton.setOnMouseClicked (dirChooser);
		entryDirButton.setOnMouseClicked (dirChooser);
		prettyFlagDirButton.setOnMouseClicked (dirChooser);
		
		addEntryButton.setOnAction (new EventHandler<ActionEvent>() {

			@Override
			public void handle (ActionEvent event) {
				try {
					EntryAdder vAdder = new EntryAdder ();
					vAdder.init (new Stage());
				} catch (Exception e) {
					e.printStackTrace ();
				}
			}
		});
		
		setVotesButton.setOnAction (new EventHandler<ActionEvent>() {
			public void handle (ActionEvent event) {
				try {
					VoteRegistrator vr = new VoteRegistrator ();
					vr.start (new Stage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings("unused") 
	private ObservableList<Participant> readConfigFile () throws IOException {
		
		String line;
		String location = System.getProperty ("user.dir") + "/...";
		ObservableList<Participant> list = FXCollections.observableArrayList ();
		
		BufferedReader reader = new BufferedReader (new FileReader (new File (location)));
		while ((line = reader.readLine ()) != null) {
			String[] tokens = line.split ("\\$");
			
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
	
	private class CellCentralizer <T> implements Callback<TableColumn<Participant, T>, TableCell<Participant, T>> {

		@Override public TableCell<Participant, T> call (TableColumn<Participant, T> tc) {
			TableCell<Participant, T> cell = new TableCell<Participant, T>() {
				
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