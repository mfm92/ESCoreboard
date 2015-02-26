package nations;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.media.Media;

public class Entry {

	private SimpleStringProperty artist;
	private SimpleStringProperty title;
	private Media media;
	private SimpleIntegerProperty start;
	private SimpleIntegerProperty stop;
	
	private SimpleStringProperty status;

	public Entry(String artist, String title, String status) {
		this.artist = new SimpleStringProperty (artist);
		this.title = new SimpleStringProperty (title);
		this.status = new SimpleStringProperty (status);
	}

	public Entry(String artist, String title, Media media, int start, int stop, String status) {
		this.artist = new SimpleStringProperty (artist);
		this.title = new SimpleStringProperty (title);
		this.media = media;
		this.start = new SimpleIntegerProperty (start);
		this.stop = new SimpleIntegerProperty (stop);
		this.status = new SimpleStringProperty (status);
	}

	public String getArtist() {
		return artist.get ();
	}
	
	public void setArtist (String artist) {
		this.artist = new SimpleStringProperty(artist);
	}

	public String getTitle() {
		return title.get ();
	}
	
	public void setTitle (String title) {
		this.title = new SimpleStringProperty(title);
	}

	public Media getMedia() {
		return media;
	}

	public int getStartDuration() {
		return start.get ();
	}

	public int getStopDuration() {
		return stop.get ();
	}
	
	public String getStatus () {
		return status.get ();
	}
	
	public void setStatus (String status) {
		this.status = new SimpleStringProperty (status);
	}
}
