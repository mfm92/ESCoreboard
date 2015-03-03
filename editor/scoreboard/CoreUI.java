package scoreboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
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
	@FXML Button removeEntryButton;
	@FXML Button setVotesButton;
	@FXML Button flagDirButton;
	@FXML Button entryDirButton;
	@FXML Button prettyFlagDirButton;
	
	@FXML MenuItem loadMenuItem;
	@FXML MenuItem saveMenuItem;
	
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
		setUpTableView ();
		setUpButtons ();
		setUpMenu ();
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
		
		removeEntryButton.setOnAction (new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				pModel.removeParticipant (pModel.getSelectedParticipant ());
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
	
	private void setUpMenu () {
		loadMenuItem.setOnAction (new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser fileChooser = new DirectoryChooser ();
				fileChooser.setInitialDirectory (new File (System.getProperty("user.dir")));
				fileChooser.setTitle ("Choose participants file...");
				File selected = fileChooser.showDialog (null);
				
				File participantsFile = new File (selected + "\\participants.txt");
				File votesFile = new File (selected + "\\votes.txt");

				try {
					readInParticipants (participantsFile);
					readInVotes (votesFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		saveMenuItem.setOnAction (new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser dirChooser = new DirectoryChooser ();
				dirChooser.setInitialDirectory (new File (System.getProperty("user.dir")));
				dirChooser.setTitle ("where to write out that shit...");
				
				try {
					writeOut (dirChooser.showDialog (null));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void readInParticipants (File inputFile) throws IOException {
		
		String line;
		
		BufferedReader reader = new BufferedReader (new FileReader (inputFile));
		while ((line = reader.readLine ()) != null) {
			String[] tokens = line.split ("\\$");
			
			Participant participant = new Participant (tokens[0], tokens[1],
					tokens[2], tokens[3], 
					Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]),
					Integer.parseInt(tokens[6]), tokens[8]);
			pModel.addParticipant (participant);
		}
			
		reader.close ();
	}
	
	private void readInVotes (File inputFile) throws IOException {
		
		String line;
		
		BufferedReader reader = new BufferedReader (new FileReader (inputFile));
		while ((line = reader.readLine ()) != null) {
			String[] tokens = line.split ("\\$");
			Participant voter = pModel.retrieveParticipantByShortName (tokens[0]);
			ArrayList<String> votes = new ArrayList<> ();
			
			for (int i = 1; i < tokens.length; i++) {
				String[] innerTokens = tokens[i].split (" ");
				votes.add (pModel.retrieveParticipantByShortName (innerTokens[1]).getName ());
			}
			
			pModel.addVotes (voter, votes);
		}
			
		reader.close ();
	}
	
	private void writeOut (File outputFile) throws FileNotFoundException {
		PrintStream participantsOut = new PrintStream (new File (outputFile + "\\participants.txt"));
		PrintStream votesOut = new PrintStream (new File (outputFile + "\\votes.txt"));
		
		final String STRING_SEPARATOR = System.lineSeparator ();
		
		for (Participant p : pModel.getParticipants ()) {
			participantsOut.append (p.getName () + "$" + p.getShortName () + "$" + 
					p.getArtist () + "$" + p.getTitle () + "$" + p.getStart () +
					"$" + p.getStop () + "$" + p.getGrid () + "$" + p.getStatus () + STRING_SEPARATOR);
		}
		
		for (Map.Entry<Participant, ArrayList<String>> vote : pModel.getVotes ().entrySet ()) {
			votesOut.append (vote.getKey ().getShortName () + "$"
					+ "12 " + pModel.getShortName (vote.getValue ().get (0)) + "$"
					+ "10 " + pModel.getShortName (vote.getValue ().get (1)) + "$"
					+ "08 " + pModel.getShortName (vote.getValue ().get (2)) + "$"
					+ "07 " + pModel.getShortName (vote.getValue ().get (3)) + "$"
					+ "06 " + pModel.getShortName (vote.getValue ().get (4)) + "$"
					+ "05 " + pModel.getShortName (vote.getValue ().get (5)) + "$"
					+ "04 " + pModel.getShortName (vote.getValue ().get (6)) + "$"
					+ "03 " + pModel.getShortName (vote.getValue ().get (7)) + "$"
					+ "02 " + pModel.getShortName (vote.getValue ().get (8)) + "$"
					+ "01 " + pModel.getShortName (vote.getValue ().get (9))
					+ STRING_SEPARATOR);
		}
		
		participantsOut.close ();
		votesOut.close ();
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