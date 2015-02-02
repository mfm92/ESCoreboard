package scoreboard;
import java.util.ArrayList;
import java.util.Collections;

import data.Standings;
import nations.Participant;
import nations.Votes;
import utilities.NSCUtilities;

public class VoteAdder implements Runnable {
	
	Standings standings;
	Scoreboard scoreboard;
	NSCUtilities utilities;
	
	ArrayList<Votes> allVotes;
	volatile int inCountryCounter;
	volatile int nrOfCountryToVote;
	
	public VoteAdder (Standings standings, Scoreboard scoreboard, NSCUtilities utilities,
			int inCountryCounter) {
		this.standings = standings;
		this.scoreboard = scoreboard;
		this.utilities = utilities;
		allVotes = utilities.allVotes;
		this.inCountryCounter = inCountryCounter;
		this.nrOfCountryToVote = (inCountryCounter-1)/10;
	}
	
	public synchronized void addVotes() throws InterruptedException {
		int nrOfPoints = 0;
		
		if (inCountryCounter%10 == 0) nrOfPoints = 12;
		else if (inCountryCounter%10 <= 8) nrOfPoints = inCountryCounter%10;
		else if (inCountryCounter%10 == 9) nrOfPoints = 10;
		
		Votes vote = allVotes.get(nrOfCountryToVote);
  		ArrayList<Participant> oldStanding = new ArrayList<>(standings.getStandings().size());
		   		
		for (Participant standing : standings.getStandings()) {
			oldStanding.add(standing.clone());
		}
		Collections.sort(oldStanding);
				
		Participant voter = vote.getVoter();
		
		if (inCountryCounter%10 == 7) {
			for (int i = 1; i <= 7; i++){
				standings.addVote(vote, i, i);
			}
		}
		else standings.addVote(vote, nrOfPoints, inCountryCounter%10);
				
		ArrayList<Participant> newStanding = new ArrayList<>();
		newStanding = standings.getStandings();
				
		scoreboard.update(newStanding, voter, oldStanding, standings);
	}

	@Override
	public synchronized void run() {
		try {
			addVotes();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
