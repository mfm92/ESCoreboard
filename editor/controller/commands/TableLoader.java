package controller.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import controller.CoreUI;
import model.ParticipantData;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.DirectoryChooser;

public class TableLoader {
	
	CoreUI coreUI;
	
	public TableLoader (CoreUI coreUI) {
		this.coreUI = coreUI;
	}

	public void execute() {
		DirectoryChooser fileChooser = new DirectoryChooser ();
		fileChooser.setInitialDirectory (new File (System.getProperty("user.dir")));
		fileChooser.setTitle ("Choose participants file...");
		File selected = fileChooser.showDialog (null);
		
		File participantsFile = new File (selected + "\\participants.txt");
		File votesFile = new File (selected + "\\votes.txt");
		File paramsFile = new File (selected + "\\params.txt");

		try {
			readInParticipants (participantsFile);
			readInVotes (votesFile);
			readInParams (paramsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readInParticipants (File inputFile) throws IOException {
		
		String line;
		
		BufferedReader reader = new BufferedReader (new FileReader (inputFile));
		while ((line = reader.readLine ()) != null) {
			String[] tokens = line.split ("\\$");
			
			ParticipantData participant = new ParticipantData (tokens[0], tokens[1],
					tokens[2], tokens[3], 
					Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]),
					Integer.parseInt(tokens[6]), tokens[7]);
			CoreUI.inputData.addParticipant (participant);
		}
			
		reader.close ();
	}
	
	private void readInVotes (File inputFile) throws IOException {
		
		String line;
		
		BufferedReader reader = new BufferedReader (new FileReader (inputFile));
		while ((line = reader.readLine ()) != null) {
			String[] tokens = line.split ("\\$");
			ParticipantData voter = CoreUI.inputData.retrieveParticipantByShortName (tokens[0]);
			ArrayList<StringProperty> votes = new ArrayList<> ();
			
			for (int i = 1; i < tokens.length; i++) {
				String[] innerTokens = tokens[i].split (" ");
				votes.add (new SimpleStringProperty (CoreUI.inputData.retrieveParticipantByShortName (innerTokens[1]).getName ()));
			}
			
			CoreUI.inputData.addVotes (voter, votes);
		}
			
		reader.close ();
	}
	
	private void readInParams (File inputFile) throws IOException {
		Properties paramFile = new Properties ();
		FileInputStream fis = new FileInputStream (inputFile);
		paramFile.load (fis);
		
		coreUI.editionName.setText (paramFile.getProperty ("NAME_EDITION"));
		coreUI.editionNumberField.setText (paramFile.getProperty ("EDITION_NR"));
		CoreUI.inputData.setFlagDirectory (paramFile.getProperty ("FLAGS_DIR"));
		CoreUI.inputData.setPrettyFlagDirectory (paramFile.getProperty ("PRETTY_FLAGS_DIR"));
		CoreUI.inputData.setEntriesDirectory (paramFile.getProperty ("ENTRIES_DIR"));
		CoreUI.inputData.setCurrentDir (paramFile.getProperty ("CURRENT_FILE_PATH"));
		coreUI.createBannersBox.setSelected (Boolean.parseBoolean (paramFile.getProperty ("CREATE_BANNERS")));
		coreUI.fullScreenBox.setSelected (Boolean.parseBoolean (paramFile.getProperty ("USE_FULLSCREEN")));
		coreUI.prettyFlagsBox.setSelected (Boolean.parseBoolean (paramFile.getProperty ("USE_PRETTY_FLAGS")));
		coreUI.traditionalVotingCheckBox.setSelected (Boolean.parseBoolean (paramFile.getProperty ("TRADITIONAL_VOTING")));
		coreUI.speedSlider.setValue (Double.parseDouble (paramFile.getProperty ("SPEED_SHOW")));
	}
}
