package nations;

import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

public class Participant implements Comparable<Participant>, Cloneable {

	private SimpleStringProperty name;
	private String shortName;
	private Image flag;
	private Votes votes;
	private Entry entry;

	private SimpleIntegerProperty score;
	private int tmpScore;

	private short totalVoters;
	private short[] receiveHistory;

	private boolean scoredFlag;

	public Participant(String name, String shortName, Image flag) {
		this.name = new SimpleStringProperty (name);
		this.shortName = shortName;
		this.flag = flag;
		this.score = new SimpleIntegerProperty (0);
		receiveHistory = new short[10];
	}

	public Participant(Participant p) {
		name = new SimpleStringProperty (p.getName ());
		shortName = p.getShortName ();
		flag = p.getFlag ();
		score = new SimpleIntegerProperty (p.getScore ());
	}

	public String getName() {
		return name.get ();
	}

	public String getShortName() {
		return shortName;
	}

	public Votes getVotes() {
		return votes;
	}

	public void setVotes(Votes votes) {
		this.votes = votes;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public Image getFlag() {
		return flag;
	}

	public int getScore() {
		return score.get ();
	}

	public int getScore(ArrayList<Participant> standings) {
		int i = 0;
		for (Participant p : standings) {
			i++;
			if (p == this)
				return standings.get (i - 1).getScore ();
		}
		return -1;
	}

	public short getTotalVoters() {
		return totalVoters;
	}

	public short[] getReceiveHistory() {
		return receiveHistory;
	}

	public int getTmpScore() {
		return tmpScore;
	}

	public void setTmpScore(int tmpScore) {
		this.tmpScore = tmpScore;
	}

	public boolean getScoredFlag() {
		return scoredFlag;
	}

	public void setScoredFlag(boolean scored) {
		scoredFlag = scored;
	}

	public void addToScore(int nrOfPointsReceived) {
		score = new SimpleIntegerProperty (score.get () + nrOfPointsReceived);
		totalVoters++;

		switch (nrOfPointsReceived) {
		case 12:
			receiveHistory[0]++;
			break;
		case 10:
			receiveHistory[1]++;
			break;
		case 8:
			receiveHistory[2]++;
			break;
		case 7:
			receiveHistory[3]++;
			break;
		case 6:
			receiveHistory[4]++;
			break;
		case 5:
			receiveHistory[5]++;
			break;
		case 4:
			receiveHistory[6]++;
			break;
		case 3:
			receiveHistory[7]++;
			break;
		case 2:
			receiveHistory[8]++;
			break;
		case 1:
			receiveHistory[9]++;
			break;
		}
	}

	@Override
	public int compareTo(Participant o) {
		if (o.getScore () > score.get ())
			return 1;
		else if (o.getScore () < score.get ())
			return -1;

		if (o.getTotalVoters () > totalVoters)
			return 1;
		else if (o.getTotalVoters () < totalVoters)
			return -1;

		for (int i = 0; i < 10; i++) {
			if (o.getReceiveHistory ()[i] > receiveHistory[i])
				return 1;
			else if (o.getReceiveHistory ()[i] < receiveHistory[i])
				return -1;
		}

		return 0;
	}

	@Override
	public Participant clone() {
		Participant clone = null;
		try {
			clone = (Participant) super.clone ();
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return clone;
	}
}
