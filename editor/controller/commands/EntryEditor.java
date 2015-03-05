package controller.commands;

import controller.CoreUI;
import model.ParticipantData;

public class EntryEditor extends EntryCommand {

	String column;
	String oldValue;
	String newValue;
	
	public EntryEditor(ParticipantData target, String column, String oldValue, String newValue) {
		super (target);
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.column = column;
	}

	@Override
	public void execute() {
		switch (column) {
			case "name": target.setName (newValue); break;
			case "shortName": target.setShortName (newValue); break;
			case "artist": target.setArtist (newValue); break;
			case "title": target.setTitle (newValue); break;
			case "start": target.setStart (newValue); break;
			case "stop": target.setStop (newValue); break;
			case "grid": target.setGrid (newValue); break;
			case "status": target.setStatus (newValue); break;
			
			default: return;
		}
	}

	@Override
	public void undo() {
		switch (column) {
			case "name": target.setName (oldValue); break;
			case "shortName": target.setShortName (oldValue); break;
			case "artist": target.setArtist (oldValue); break;
			case "title": target.setTitle (oldValue); break;
			case "start": target.setStart (oldValue); break;
			case "stop": target.setStop (oldValue); break;
			case "grid": target.setGrid (oldValue); break;
			case "status": target.setStatus (oldValue); break;
		
			default: return;
		}
		
		CoreUI.commandPtr--;
	}

}
