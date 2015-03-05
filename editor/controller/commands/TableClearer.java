package controller.commands;

import controller.CoreUI;
import model.ParticipantData;

public class TableClearer {

	public void execute() {
		for (ParticipantData pData : CoreUI.inputData.getParticipants()) {
			CoreUI.inputData.removeVotes (pData);
			CoreUI.inputData.removeParticipant (pData);
		}
	}
}
