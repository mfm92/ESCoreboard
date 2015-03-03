package model;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ParticipantModel {
	
	Property<ObservableList<Participant>> participants = new SimpleObjectProperty<> ();
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
	
	public Property<ObservableList<Participant>> getPProp () {
		return participants;
	}
	
	public void addParticipant (Participant p) {
		participants.getValue().add (p);
	}
}