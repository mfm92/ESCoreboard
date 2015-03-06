package controller.commands;

import java.io.File;
import java.io.IOException;

import javafx.stage.FileChooser;

public class DataSaver {

	EntryWriter writer;
	File destination;
	
	public DataSaver (EntryWriter writer, File destination) {
		this.writer = writer;
		this.destination = destination;
	}

	public void save (boolean saveAs) throws IOException {
		File toSave = null;
		
		if (destination == null || saveAs) {
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter ("XSCO Data (*.xsco)", "*.xsco");
			fileChooser.getExtensionFilters ().add (extFilter);
			fileChooser.setInitialDirectory (new File (System.getProperty("user.dir")));
			fileChooser.setTitle ("where to write out that shit...");
			
			toSave = fileChooser.showSaveDialog (null);
			destination = toSave;
		} else {
			toSave = destination;
		}
		
		writer.writeOut (toSave);
	}
}
