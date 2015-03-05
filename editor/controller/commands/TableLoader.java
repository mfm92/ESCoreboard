package controller.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javafx.stage.FileChooser;
import model.ParticipantData;
import controller.CoreUI;

public class TableLoader {
	
	CoreUI coreUI;
	
	public TableLoader (CoreUI coreUI) {
		this.coreUI = coreUI;
	}

	public void execute() throws FileNotFoundException, IOException {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter ("XSCO Data (*.xsco)", "*.xsco");
		fileChooser.getExtensionFilters ().add (extFilter);
		fileChooser.setInitialDirectory (new File (System.getProperty("user.dir")));
		fileChooser.setTitle ("Choose participants file...");
		
		String origin = null;
		try (BufferedReader bReader = new BufferedReader (new FileReader (fileChooser.showOpenDialog (null)))) {
			origin = bReader.readLine ();
		}
		
		File participantsFile = new File (System.getProperty("user.dir") + "\\resources\\save\\participants_" + origin + ".txt");
		File votesFile = new File (System.getProperty("user.dir") + "\\resources\\save\\votes_" + origin + ".txt");
		File paramsFile = new File (System.getProperty("user.dir") + "\\resources\\save\\params_" + origin + ".txt");

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
					tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7]);
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
			ArrayList<ParticipantData> votes = new ArrayList<> ();
			
			for (int i = 1; i < tokens.length; i++) {
				String[] innerTokens = tokens[i].split (" ");
				votes.add (CoreUI.inputData.retrieveParticipantByShortName (innerTokens[1]));
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
		coreUI.createBannersBox.setSelected (Boolean.parseBoolean (paramFile.getProperty ("CREATE_BANNERS")));
		coreUI.fullScreenBox.setSelected (Boolean.parseBoolean (paramFile.getProperty ("USE_FULLSCREEN")));
		coreUI.prettyFlagsBox.setSelected (Boolean.parseBoolean (paramFile.getProperty ("USE_PRETTY_FLAGS")));
		coreUI.traditionalVotingCheckBox.setSelected (Boolean.parseBoolean (paramFile.getProperty ("TRADITIONAL_VOTING")));
		coreUI.speedSlider.setValue (Double.parseDouble (paramFile.getProperty ("SPEED_SHOW")));
	}
}
