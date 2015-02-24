package data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import nations.Participant;
import nations.Votes;
import utilities.NSCUtilities;

public class Standings {

	private ArrayList<Participant> standings;
	private ArrayList<Votes> votes;

	public Standings(NSCUtilities utilities) throws FileNotFoundException,
			IOException {
		setStandings(new ArrayList<Participant>());
		String[] finalists = utilities.getListOfNames();
		for (Participant nation : utilities.getListOfNations(finalists)) {
			getStandings().add(nation);
		}
		Collections.sort(getStandings());
		setVotes(utilities.allVotes);
	}

	public void addVote(Votes votes, int nrOfPoints, int inCountryCounter) {
		Participant receiver = null;

		if (inCountryCounter >= 1)
			receiver = votes.getReceivers()[inCountryCounter - 1];
		else
			receiver = votes.getReceivers()[votes.getReceivers().length - 1];

		receiver.addToScore(nrOfPoints);
		Collections.sort(getStandings());
	}

	public synchronized int getPosition(ArrayList<Participant> standings,
			Participant participant) {

		ArrayList<Participant> deepCopy = new ArrayList<>();
		for (Participant part : standings) {
			deepCopy.add(part.clone());
		}

		int position = 1;
		for (Participant part : deepCopy) {
			if (part.getName().equals(participant.getName()))
				return position;
			position++;
		}
		return -564508407; // #sign
	}

	public ArrayList<Votes> getVotes() {
		return votes;
	}

	public void setVotes(ArrayList<Votes> votes) {
		this.votes = votes;
	}

	public ArrayList<Participant> getStandings() {
		return standings;
	}

	public void setStandings(ArrayList<Participant> standings) {
		this.standings = standings;
	}
}
