package controller.commands;

import java.io.File;
import java.io.FileNotFoundException;

import javafx.stage.DirectoryChooser;

public class DataSaver implements Saver {

	EntryWriter writer;
	File destination;
	
	public DataSaver (EntryWriter writer, File destination) {
		this.writer = writer;
		this.destination = destination;
	}

	@Override
	public void save() {
		File toSave = null;
		
		if (destination == null) {
			DirectoryChooser dirChooser = new DirectoryChooser ();
			dirChooser.setInitialDirectory (new File (System.getProperty("user.dir")));
			dirChooser.setTitle ("where to write out that shit...");
			toSave = dirChooser.showDialog (null);
			destination = toSave;
		} else {
			toSave = destination;
		}
		
		if (toSave == null) return;
		
		try {
			writer.writeOut (toSave);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
