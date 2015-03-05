package model;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InputDataModel {
	
	Property<ObservableList<ParticipantData>> participants = new SimpleObjectProperty<> ();
	IntegerProperty selectedIndex = new SimpleIntegerProperty ();
	
	/*
	 * Votes are ordered as follows: ArrayList<String>.get(0) returns 12 pointer, etc.
	 */
	Property<HashMap<ParticipantData, ArrayList<ParticipantData>>> votes = new SimpleObjectProperty<> ();
	
	StringProperty nameOfEdition = new SimpleStringProperty ();
	StringProperty editionNr = new SimpleStringProperty ();
	
	StringProperty flagDirectory = new SimpleStringProperty ();
	StringProperty entriesDirectory = new SimpleStringProperty ();
	StringProperty prettyFlagDirectory = new SimpleStringProperty ();
	
	BooleanProperty traditionalVoting = new SimpleBooleanProperty ();
	BooleanProperty bannerCreatorActivated = new SimpleBooleanProperty ();
	BooleanProperty useFullScreen = new SimpleBooleanProperty ();
	BooleanProperty usePrettyFlags = new SimpleBooleanProperty ();
	
	DoubleProperty showSpeed = new SimpleDoubleProperty ();
	
	public InputDataModel () {
		participants.setValue (FXCollections.<ParticipantData> observableArrayList ());
		votes.setValue (new HashMap<ParticipantData, ArrayList<ParticipantData>>());
	}

	public ArrayList<ParticipantData> getParticipants() {
		ArrayList<ParticipantData> copy = new ArrayList<ParticipantData> (participants.getValue ());
		System.out.println (copy.size ());
		return copy;
	}
	
	public void setParticipants (ObservableList<ParticipantData> ps) {
		participants.setValue (ps);
	}
	
	public Property<ObservableList<ParticipantData>> getParticipantsProperty () {
		return participants;
	}
	
	public void addParticipant (ParticipantData p) {
		participants.getValue().add (p);
	}
	
	public void removeParticipant (ParticipantData p) {
		participants.getValue ().remove (p);
	}
	
	public ParticipantData retrieveParticipantByShortName (String shortName) {
		for (ParticipantData p : participants.getValue ()) {
			if (p.getShortName ().equals (shortName)) return p;
		}
		return null;
	}
	
	public String getShortName (String pName) {
		ParticipantData pa = null;
		
		for (ParticipantData p : participants.getValue ()) {
			if (p.getName ().equals (pName)) pa = p;
		}
		
		return pa == null ? null : pa.getShortName ();
	}
	
	public int getSelectedIndex () {
		return selectedIndex.get ();
	}
	
	public IntegerProperty getSelectedIndexProperty () {
		return selectedIndex;
	}
	
	public void setSelectedIndex (int index) {
		selectedIndex.set (index);
	}
	
	public ParticipantData getSelectedParticipant () {
		return participants.getValue ().get (selectedIndex.get ());
	}
	
	public ParticipantData getSelectedParticipantClone () {
		return participants.getValue ().get (selectedIndex.get ()).clone();
	}
	
	public HashMap<ParticipantData, ArrayList<ParticipantData>> getVotes () {
		return votes.getValue ();
	}
	
	public Property<HashMap<ParticipantData, ArrayList<ParticipantData>>> getVoteProperty () {
		return votes;
	}
	
	public void setVotes (HashMap<ParticipantData, ArrayList<ParticipantData>> votes) {
		this.votes.setValue (votes);
		
		// TODO: Set votes for everyone
	}
	
	public void addVotes (ParticipantData p, ArrayList<ParticipantData> votes) {
		this.votes.getValue ().put (p, votes);
		
		// TODO: Set votes
	}
	
	public void addVotes (String v, ArrayList<ParticipantData> votes) {		
		this.votes.getValue ().put (findParticipantByName (v), votes);
		
		// TODO: Set votes
	}
	
	public ParticipantData findParticipantByName (String name) {
		ParticipantData participant = null;
		
		for (ParticipantData p : participants.getValue ()) {
			if (p.getName ().equals (name)) participant = p;
		}
		
		return participant;
	}
	
	public void removeVotes (ParticipantData p) {
		votes.getValue ().remove (p);
		
		// TODO: Remove votes
	}
	
	public String getNameOfEdition () {
		return nameOfEdition.get ();
	}
	
	public StringProperty getNameOfEditionProperty () {
		return nameOfEdition;
	}
	
	public void setNameOfEdition (String nameOfEdition) {
		this.nameOfEdition.set (nameOfEdition);
	}
	
	public String getEditionNr () {
		return editionNr.get ();
	}
	
	public StringProperty getEditionNrProperty () {
		return editionNr;
	}
	
	public void setEditionNr (String editionNr) {
		this.editionNr.set (editionNr);
	}
	
	public String getFlagDirectory () {
		return flagDirectory.get ();
	}
	
	public StringProperty getFlagDirectoryProperty () {
		return flagDirectory;
	}
	
	public void setFlagDirectory (String flagDirectory) {
		this.flagDirectory.set (flagDirectory);
	}
	
	public String getEntriesDirectory () {
		return entriesDirectory.get ();
	}
	
	public StringProperty getEntriesDirectoryProperty () {
		return entriesDirectory;
	}
	
	public void setEntriesDirectory (String entriesDirectory) {
		this.entriesDirectory.set (entriesDirectory);
	}
	
	public String getPrettyFlagDirectory () {
		return prettyFlagDirectory.get ();
	}
	
	public StringProperty getPrettyFlagDirectoryProperty () {
		return prettyFlagDirectory;
	}
	
	public void setPrettyFlagDirectory (String prettyFlagDirectory) {
		this.prettyFlagDirectory.set (prettyFlagDirectory);
	}
	
	public boolean getTraditionalVoting () {
		return traditionalVoting.get ();
	}
	
	public BooleanProperty getTraditionalVotingProperty () {
		return traditionalVoting;
	}
	
	public void setTraditionalVoting (boolean traditionalVoting) {
		this.traditionalVoting.set (traditionalVoting);
	}
	
	public boolean getBannerCreatorActivated () {
		return bannerCreatorActivated.get ();
	}
	
	public BooleanProperty getBannerCreatorActivatedProperty () {
		return bannerCreatorActivated;
	}
	
	public void setBannerCreatorActivated (boolean bannerCreatorActivated) {
		this.bannerCreatorActivated.set (bannerCreatorActivated);
	}
	
	public boolean getUseFullScreen () {
		return useFullScreen.get ();
	}
	
	public BooleanProperty getUseFullScreenProperty () {
		return useFullScreen;
	}
	
	public void setUseFullScreen (boolean useFullScreen) {
		this.useFullScreen.set (useFullScreen);
	}
	
	public boolean getUsePrettyFlags () {
		return usePrettyFlags.get ();
	}
	
	public BooleanProperty getUsePrettyFlagsProperty () {
		return usePrettyFlags;
	}
	
	public void setUsePrettyFlags (boolean usePrettyFlags) {
		this.usePrettyFlags.set (usePrettyFlags);
	}
	
	public double getShowSpeed () {
		return showSpeed.get ();
	}
	
	public DoubleProperty getShowSpeedProperty () {
		return showSpeed;
	}
	
	public void setShowSpeed (double speed) {
		showSpeed.set (speed);
	}
}