package controller.commands;

import java.util.ArrayList;

import javafx.beans.property.StringProperty;
import model.ParticipantData;
import controller.CoreUI;

public class VoteSetter extends EntryCommand {

	ArrayList<StringProperty> votes;
	
	public VoteSetter(ParticipantData target, ArrayList<StringProperty> votes) {
		super (target);
		this.votes = votes;
	}

	@Override
	public void execute() {
		CoreUI.inputData.addVotes (target, votes);
	}

	@Override
	public void undo() {
		CoreUI.inputData.removeVotes (target);
		CoreUI.commandPtr--;
	}
}
