package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ParticipantData {
	
	StringProperty name;
	StringProperty shortName;
	StringProperty artist;
	StringProperty title;
	IntegerProperty start;
	IntegerProperty stop;
	IntegerProperty grid;
	StringProperty status;
	
	public ParticipantData (String name, String shortName, String artist,
			String title, int start, int stop, int grid, String status) {

		this.name = new SimpleStringProperty(name);
		this.shortName = new SimpleStringProperty(shortName);
		this.artist = new SimpleStringProperty(artist);
		this.title = new SimpleStringProperty(title);
		this.start = new SimpleIntegerProperty (start);
		this.stop = new SimpleIntegerProperty (stop);
		this.grid = new SimpleIntegerProperty (grid);
		this.status = new SimpleStringProperty (status);
	}
	
	public ParticipantData clone() {
		return new ParticipantData (name.get (), shortName.get (), artist.get (),
				title.get (), start.get (), stop.get (), grid.get (), status.get ());
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name = new SimpleStringProperty (name);
	}
	
	public StringProperty getNameProperty () {
		return name;
	}

	public String getShortName() {
		return shortName.get();
	}

	public void setShortName(String shortName) {
		this.shortName = new SimpleStringProperty(shortName);
	}

	public String getArtist() {
		return artist.get();
	}

	public void setArtist(String artist) {
		this.artist = new SimpleStringProperty(artist);
	}

	public String getTitle() {
		return title.get();
	}

	public void setTitle(String title) {
		this.title = new SimpleStringProperty(title);
	}

	public int getStart() {
		return start.get();
	}

	public void setStart(int start) {
		this.start = new SimpleIntegerProperty(start);
	}

	public int getStop() {
		return stop.get();
	}

	public void setStop(int stop) {
		this.stop = new SimpleIntegerProperty(stop);
	}

	public int getGrid() {
		return grid.get();
	}

	public void setGrid(int grid) {
		this.grid = new SimpleIntegerProperty(grid);
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(String status) {
		this.status = new SimpleStringProperty(status);
	}
}
