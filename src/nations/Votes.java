package nations;

import utilities.NSCUtilities;

public class Votes {

	private Participant voter;
	private Participant[] receivers = new Participant[10];

	public Votes(String voter, String P1, String P2, String P3, String P4,
			String P5, String P6, String P7, String P8, String P10, String P12,
			NSCUtilities utilities) {
		this.voter = utilities.getRosterNationByShortName(voter);
		receivers[0] = utilities.getRosterNationByShortName(P1);
		receivers[1] = utilities.getRosterNationByShortName(P2);
		receivers[2] = utilities.getRosterNationByShortName(P3);
		receivers[3] = utilities.getRosterNationByShortName(P4);
		receivers[4] = utilities.getRosterNationByShortName(P5);
		receivers[5] = utilities.getRosterNationByShortName(P6);
		receivers[6] = utilities.getRosterNationByShortName(P7);
		receivers[7] = utilities.getRosterNationByShortName(P8);
		receivers[8] = utilities.getRosterNationByShortName(P10);
		receivers[9] = utilities.getRosterNationByShortName(P12);
	}

	public Votes(Participant voter, Participant P1, Participant P2,
			Participant P3, Participant P4, Participant P5, Participant P6,
			Participant P7, Participant P8, Participant P10, Participant P12) {
		this.voter = voter;
		receivers[0] = P1;
		receivers[1] = P2;
		receivers[2] = P3;
		receivers[3] = P4;
		receivers[4] = P5;
		receivers[5] = P6;
		receivers[6] = P7;
		receivers[7] = P8;
		receivers[8] = P10;
		receivers[9] = P12;
	}

	public Participant getVoter() {
		return voter;
	}

	public Participant[] getReceivers() {
		return receivers;
	}
}
