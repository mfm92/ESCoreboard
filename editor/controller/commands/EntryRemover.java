package controller.commands;

import model.ParticipantData;
import controller.CoreUI;

public class EntryRemover extends EntryCommand {

	public EntryRemover(ParticipantData target) {
		super (target);
	}

	@Override
	public void execute() {
		CoreUI.inputData.removeParticipant (target);
	}

	@Override
	public void undo() {
		CoreUI.inputData.addParticipant (target);
		CoreUI.commandPtr--;
	}
}
