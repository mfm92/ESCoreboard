package controller;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.ParticipantData;
import model.InputDataModel;

public class CoreUI extends Application implements Initializable {
	
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
	
	@FXML MenuItem loadMenuItem;
	@FXML MenuItem saveMenuItem;
	
	@FXML TextField editionName;
	@FXML TextField editionNumberField;
	
	@FXML CheckBox createBannersBox;
	@FXML CheckBox traditionalVotingCheckBox;
	@FXML CheckBox fullScreenBox;
	@FXML CheckBox prettyFlagsBox;
	
	static InputDataModel inputData = new InputDataModel();
	
	public static void main(String[] args) {
		launch (args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {	
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation (getClass ().getResource ("/view/CoreUI.fxml"));
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
		
		nationNameCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("name"));
		shortnameCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("shortName"));
		artistCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("artist"));
		titleCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("title"));
		startCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, Integer>("start"));
		stopCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, Integer>("stop"));
		gridCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, Integer>("grid"));
		statusCol.setCellValueFactory (new PropertyValueFactory<ParticipantData, String> ("status"));
		
		nationNameCol.setCellFactory (textCentralizer);
		shortnameCol.setCellFactory (textCentralizer);
		artistCol.setCellFactory (textCentralizer);
		titleCol.setCellFactory (textCentralizer);
		startCol.setCellFactory (intCentralizer);
		stopCol.setCellFactory (intCentralizer);
		gridCol.setCellFactory (intCentralizer);
		statusCol.setCellFactory (textCentralizer);

		table.itemsProperty ().bind (inputData.getParticipantsProperty ());
		inputData.getSelectedIndexProperty ().bind (table.getSelectionModel ().selectedIndexProperty ());
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
				inputData.removeParticipant (inputData.getSelectedParticipant ());
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
			
			ParticipantData participant = new ParticipantData (tokens[0], tokens[1],
					tokens[2], tokens[3], 
					Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]),
					Integer.parseInt(tokens[6]), tokens[8]);
			inputData.addParticipant (participant);
		}
			
		reader.close ();
	}
	
	private void readInVotes (File inputFile) throws IOException {
		
		String line;
		
		BufferedReader reader = new BufferedReader (new FileReader (inputFile));
		while ((line = reader.readLine ()) != null) {
			String[] tokens = line.split ("\\$");
			ParticipantData voter = inputData.retrieveParticipantByShortName (tokens[0]);
			ArrayList<String> votes = new ArrayList<> ();
			
			for (int i = 1; i < tokens.length; i++) {
				String[] innerTokens = tokens[i].split (" ");
				votes.add (inputData.retrieveParticipantByShortName (innerTokens[1]).getName ());
			}
			
			inputData.addVotes (voter, votes);
		}
			
		reader.close ();
	}
	
	private void writeOut (File outputFile) throws FileNotFoundException {
		PrintStream participantsOut = new PrintStream (new File (outputFile + "\\participants.txt"));
		PrintStream votesOut = new PrintStream (new File (outputFile + "\\votes.txt"));
		
		final String STRING_SEPARATOR = System.lineSeparator ();
		
		for (ParticipantData p : inputData.getParticipants ()) {
			participantsOut.append (p.getName () + "$" + p.getShortName () + "$" + 
					p.getArtist () + "$" + p.getTitle () + "$" + p.getStart () +
					"$" + p.getStop () + "$" + p.getGrid () + "$" + p.getStatus () + STRING_SEPARATOR);
		}
		
		for (Map.Entry<ParticipantData, ArrayList<String>> vote : inputData.getVotes ().entrySet ()) {
			votesOut.append (vote.getKey ().getShortName () + "$"
					+ "12 " + inputData.getShortName (vote.getValue ().get (0)) + "$"
					+ "10 " + inputData.getShortName (vote.getValue ().get (1)) + "$"
					+ "08 " + inputData.getShortName (vote.getValue ().get (2)) + "$"
					+ "07 " + inputData.getShortName (vote.getValue ().get (3)) + "$"
					+ "06 " + inputData.getShortName (vote.getValue ().get (4)) + "$"
					+ "05 " + inputData.getShortName (vote.getValue ().get (5)) + "$"
					+ "04 " + inputData.getShortName (vote.getValue ().get (6)) + "$"
					+ "03 " + inputData.getShortName (vote.getValue ().get (7)) + "$"
					+ "02 " + inputData.getShortName (vote.getValue ().get (8)) + "$"
					+ "01 " + inputData.getShortName (vote.getValue ().get (9))
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