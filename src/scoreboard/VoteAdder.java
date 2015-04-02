package scoreboard;

import java.util.ArrayList;
import java.util.Collections;

import data.Standings;
import nations.Participant;
import nations.Votes;
import utilities.Utilities;

public class VoteAdder implements Runnable {

	Standings standings;
	Scoreboard scoreboard;
	Utilities utilities;

	ArrayList<Votes> allVotes;
	volatile int inCountryCounter;
	volatile int nrOfCountryToVote;
	
	boolean tradVP;

	public VoteAdder(Standings standings, Scoreboard scoreboard,
			Utilities utilities, int inCountryCounter, boolean tradVP) {
		this.standings = standings;
		this.scoreboard = scoreboard;
		this.utilities = utilities;
		allVotes = utilities.allVotes;
		this.inCountryCounter = inCountryCounter;
		this.nrOfCountryToVote = (inCountryCounter - 1) / 10;
		this.tradVP = tradVP;
	}

	public void addVotes() {
		int nrOfPoints = 0;

		if (inCountryCounter % 10 == 0)
			nrOfPoints = 12;
		else if (inCountryCounter % 10 <= 8)
			nrOfPoints = inCountryCounter % 10;
		else if (inCountryCounter % 10 == 9)
			nrOfPoints = 10;

		Votes vote = allVotes.get (nrOfCountryToVote);
		ArrayList<Participant> oldStanding = new ArrayList<> (standings.getStandings ().size ());

		for (Participant standing : standings.getStandings ()) {
			oldStanding.add (standing.clone ());
		}
		Collections.sort (oldStanding);

		Participant voter = vote.getVoter ();

		if (!tradVP) {
			if (inCountryCounter % 10 == scoreboard.getTransParts()) {
				for (int i = 1; i <= scoreboard.getTransParts(); i++) {
					standings.addVote (vote, scoreboard.indicesToPoints (i), i);
				}
			} else
				standings.addVote (vote, nrOfPoints, inCountryCounter % 10);	
		} else {
			standings.addVote (vote, nrOfPoints, inCountryCounter % 10);
		}

		ArrayList<Participant> newStanding = new ArrayList<> ();
		newStanding = standings.getStandings ();

		scoreboard.update (newStanding, voter, oldStanding, standings);
	}

	@Override
	public void run() {
		addVotes();
	}
}
