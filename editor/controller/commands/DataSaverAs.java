package controller.commands;

import java.io.File;
import java.io.FileNotFoundException;

import javafx.stage.DirectoryChooser;

public class DataSaverAs implements Saver {

	EntryWriter writer;
	
	public DataSaverAs (EntryWriter writer) {
		this.writer = writer;
	}

	@Override
	public void save() {
		System.out.println ("Inner call!");
		DirectoryChooser dirChooser = new DirectoryChooser ();
		dirChooser.setInitialDirectory (new File (System.getProperty("user.dir")));
		dirChooser.setTitle ("where to write out that shit...");
		
		File destination = dirChooser.showDialog (null);
		
		try {
			writer.writeOut (destination);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}