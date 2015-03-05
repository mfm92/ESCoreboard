package controller.commands;

import model.ParticipantData;
import controller.CoreUI;

public class EntryAdder extends EntryCommand {
	
	public EntryAdder(ParticipantData target) {
		super (target);
	}

	@Override
	public void execute() {
		CoreUI.inputData.addParticipant (target);
	}

	@Override
	public void undo() {
		CoreUI.inputData.removeParticipant (target);
		CoreUI.commandPtr--;
	}
}
