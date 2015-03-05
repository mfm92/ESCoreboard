package controller.commands;

import java.util.LinkedList;
import java.util.List;

import controller.CoreUI;
import model.ParticipantData;

public class MacroCommand extends EntryCommand {
	
	private List<EntryCommand> commands = new LinkedList<>();
	
	public MacroCommand (ParticipantData target) {
		super (target);
	}
	
	public List<EntryCommand> getCommands () {
		return commands;
	}

	@Override
	public void execute() {
		for (EntryCommand command : commands) {
			command.execute ();
		}
	}
	
	@Override
	public void undo () {
		for (EntryCommand command : commands) {
			command.undo ();
		}
		CoreUI.commandPtr += (commands.size () - 1);
	}
}