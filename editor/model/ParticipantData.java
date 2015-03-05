package model;

import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class ParticipantData {
	
	StringProperty name;
	StringProperty shortName;
	StringProperty artist;
	StringProperty title;
	StringProperty start;
	StringProperty stop;
	StringProperty grid;
	StringProperty status;
	
	ListProperty<ParticipantData> votes;
	
	public ParticipantData (String name, String shortName, String artist,
			String title, String start, String stop, String grid, String status) {

		this.name = new SimpleStringProperty(name);
		this.shortName = new SimpleStringProperty(shortName);
		this.artist = new SimpleStringProperty(artist);
		this.title = new SimpleStringProperty(title);
		this.start = new SimpleStringProperty (start);
		this.stop = new SimpleStringProperty (stop);
		this.grid = new SimpleStringProperty (grid);
		this.status = new SimpleStringProperty (status);
		
		votes = new SimpleListProperty<> ();
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

	public String getStart() {
		return start.get();
	}

	public void setStart(String start) {
		this.start = new SimpleStringProperty(start);
	}

	public String getStop() {
		return stop.get();
	}

	public void setStop(String stop) {
		this.stop = new SimpleStringProperty(stop);
	}

	public String getGrid() {
		return grid.get();
	}

	public void setGrid(String grid) {
		this.grid = new SimpleStringProperty(grid);
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(String status) {
		this.status = new SimpleStringProperty(status);
	}
	
	public List<ParticipantData> getVotes () {
		return votes.get ();
	}
	
	public ListProperty<ParticipantData> getVoteProperty () {
		return votes;
	}
	
	public void setVotes (List<ParticipantData> votes) {
		this.votes.set ((ObservableList<ParticipantData>) votes);
	}
	
	public String toString () {
		return name.get ();
	}
}