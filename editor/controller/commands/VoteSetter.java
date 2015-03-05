package controller.commands;

import java.util.ArrayList;

import model.ParticipantData;
import controller.CoreUI;

public class VoteSetter extends EntryCommand {

	ArrayList<ParticipantData> votes;
	
	public VoteSetter(ParticipantData target, ArrayList<ParticipantData> votes) {
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
