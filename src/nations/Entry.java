package nations;
import javafx.scene.media.Media;

public class Entry {
	
	private String artist;
	private String title;
	private Media media;
	private int start;
	private int stop;
	
	public Entry (String artist, String title) {
		this.artist = artist;
		this.title = title;
	}
	
	public Entry (String artist, String title, Media media, int start, int stop) {
		this.artist = artist;
		this.title = title;
		this.media = media;
		this.start = start;
		this.stop = stop;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getTitle() {
		return title;
	}

	public Media getMedia() {
		return media;
	}
	
	public int getStartDuration() {
		return start;
	}
	
	public int getStopDuration() {
		return stop;
	}
}
