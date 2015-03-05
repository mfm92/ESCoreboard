package controller.commands;

import model.ParticipantData;

public abstract class EntryCommand {
	
	ParticipantData target;
	
	public EntryCommand (ParticipantData target) {
		this.target = target;
	}
	
	public abstract void execute ();
	public abstract void undo ();
}
