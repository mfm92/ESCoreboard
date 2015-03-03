package model;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ParticipantModel {
	
	Property<ObservableList<Participant>> participants = new SimpleObjectProperty<> ();
	IntegerProperty selectedIndex = new SimpleIntegerProperty ();
	
	/*
	 * Votes are ordered as follows: ArrayList<String>.get(0) returns 12 pointer, etc.
	 */
	Property<HashMap<Participant, ArrayList<String>>> votes = new SimpleObjectProperty<> ();
	
	public ParticipantModel () {
		participants.setValue (FXCollections.<Participant> observableArrayList ());
		votes.setValue (new HashMap<Participant, ArrayList<String>>());
	}

	public ArrayList<Participant> getParticipants() {
		ArrayList<Participant> copy = new ArrayList<Participant> (participants.getValue ());
		System.out.println (copy.size ());
		return copy;
	}
	
	public void setParticipants (ObservableList<Participant> ps) {
		participants.setValue (ps);
	}
	
	public Property<ObservableList<Participant>> getParticipantsProperty () {
		return participants;
	}
	
	public void addParticipant (Participant p) {
		participants.getValue().add (p);
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
	
	public Participant getSelectedParticipant () {
		return participants.getValue ().get (selectedIndex.get ());
	}
	
	public HashMap<Participant, ArrayList<String>> getVotes () {
		return votes.getValue ();
	}
	
	public Property<HashMap<Participant, ArrayList<String>>> getVoteProperty () {
		return votes;
	}
	
	public void setVotes (HashMap<Participant, ArrayList<String>> votes) {
		this.votes.setValue (votes);
	}
	
	public void addVotes (Participant p, ArrayList<String> votes) {
		this.votes.getValue ().put (p, votes);
	}
	
	public void addVotes (String v, ArrayList<String> votes) {
		Participant voter = null;
		
		for (Participant p : participants.getValue ()) {
			if (p.getName ().equals (v)) voter = p;
		}
		
		if (voter == null) return;
		
		this.votes.getValue ().put (voter, votes);
	}
}