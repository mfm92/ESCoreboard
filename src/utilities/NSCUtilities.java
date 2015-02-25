package utilities;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import javax.imageio.ImageIO;

import nations.Entry;
import nations.Participant;
import nations.Votes;
import bannercreator.BannerCreator;
import bannercreator.SimpleBannerCreator;

public class NSCUtilities {

	public Image nationTileBackground;
	public Image nationTileBackgroundScored;
	public Image nationTileBackgroundPQ;
	public Image nationTileBackgroundPQScored;
	public Image nationTileBackgroundVoter;
	public Image voterPointToken;

	public Image pointsTileBackground;
	public Image pointsTileBackgroundPQ;
	public Image pointsTileBackgroundVoter;

	public Image backgroundWhite;
	public Image backgroundRed;
	public Image backgroundBlue;

	public Image pprais;
	public Image ppraisbg;
	
	public Image voteUnderlay;
	public Image voteFlagUnderlay;
	public Image voteNameUnderlay;
	public Image voteCounterUL;
	public Image voteCounterULSmall;

	public HashMap<String, Participant> nameMap;
	public HashMap<Participant, Votes> voteMap;
	public HashMap<Participant, Image> diamondMap;

	public ArrayList<Participant> participants = new ArrayList<Participant> ();
	
	boolean readDiamonds;

	BannerCreator bannerCreator = new SimpleBannerCreator (130, 810);

	private ArrayList<Image> pointsTokens;
	public ArrayList<Votes> allVotes;
	
	String resourcesFile = System.getProperty ("user.dir") + "\\";

	public NSCUtilities(boolean readDiamonds) throws IOException {
		initialize ();
		this.readDiamonds = readDiamonds;
	}

	public void initialize() throws IOException {
		nameMap = new HashMap<String, Participant> ();
		voteMap = new HashMap<Participant, Votes> ();
		diamondMap = new HashMap<> ();

		readUtilImages ();
		readPtsTokens ();
		readNations ();
		readVotes ();
		readEntries ();
		createBanners ();
	}

	public void readUtilImages() throws IOException {
		String resourcesFile = this.resourcesFile + "resources\\";

		nationTileBackground = readImage (resourcesFile
				+ "Graphics\\Scoreboard Single Nation Backgrounds\\BG.png");
		nationTileBackgroundScored = readImage (resourcesFile
				+ "Graphics\\Scoreboard Single Nation Backgrounds\\Scored_BG.png");
		nationTileBackgroundPQ = readImage (resourcesFile
				+ "Graphics\\Scoreboard Single Nation Backgrounds\\PQ_BG.png");
		nationTileBackgroundPQScored = readImage (resourcesFile
				+ "Graphics\\Scoreboard Single Nation Backgrounds\\PQ_Scored_BG.png");
		nationTileBackgroundVoter = readImage (resourcesFile
				+ "Graphics\\Scoreboard Single Nation Backgrounds\\Voter_BG.png");

		pointsTileBackground = readImage (resourcesFile
				+ "Graphics\\Point Tokens\\BluePtsBG.png");
		pointsTileBackgroundPQ = readImage (resourcesFile
				+ "Graphics\\Point Tokens\\RedPtsBG.png");

		backgroundWhite = readImage (resourcesFile
				+ "Graphics\\Global Backgrounds\\Scoreboard BG BW.png");
		backgroundBlue = readImage (resourcesFile
				+ "Graphics\\Global Backgrounds\\Scoreboard BG Blue.png");
		backgroundRed = readImage (resourcesFile
				+ "Graphics\\Global Backgrounds\\Scoreboard BG Red.png");

		pprais = readImage (resourcesFile + "Graphics\\Point Tokens\\12PPrais.png");
		ppraisbg = readImage (resourcesFile
				+ "Graphics\\Scoreboard Single Nation Backgrounds\\Praise_BG.png");

		voterPointToken = readImage (resourcesFile
				+ "Graphics\\Point Tokens\\Calling.png");
		
		voteUnderlay = readImage (resourcesFile + "Graphics\\SideBarTokens\\VotingBGVoter.png");
		voteFlagUnderlay = readImage (resourcesFile + "Graphics\\SideBarTokens\\VotingBGFlag.png");
		voteNameUnderlay = readImage (resourcesFile + "Graphics\\SideBarTokens\\VotingBGName.png");
		voteCounterUL = readImage (resourcesFile + "Graphics\\SideBarTokens\\VotingBGCounter.png");
		voteCounterULSmall = readImage (resourcesFile + "Graphics\\SideBarTokens\\VotingBGCounterSmall.png");
	}

	private Image readImage(String fileName) throws IOException {
		BufferedImage newBuffImg = null;
		newBuffImg = ImageIO.read (new File (fileName));
		return SwingFXUtils.toFXImage (newBuffImg, null);
	}

	public void readPtsTokens() throws IOException {
		ArrayList<Image> pToken = new ArrayList<> ();

		String baseLocation = resourcesFile + "resources\\Graphics\\Point Tokens\\";

		String P01l = baseLocation + "1P.png";
		String P02l = baseLocation + "2P.png";
		String P03l = baseLocation + "3P.png";
		String P04l = baseLocation + "4P.png";
		String P05l = baseLocation + "5P.png";
		String P06l = baseLocation + "6P.png";
		String P07l = baseLocation + "7P.png";
		String P08l = baseLocation + "8P.png";
		String P10l = baseLocation + "10P.png";
		String P12l = baseLocation + "12P.png";

		pToken.add (SwingFXUtils.toFXImage (ImageIO.read (new File (P01l)),
				null));
		pToken.add (SwingFXUtils.toFXImage (ImageIO.read (new File (P02l)),
				null));
		pToken.add (SwingFXUtils.toFXImage (ImageIO.read (new File (P03l)),
				null));
		pToken.add (SwingFXUtils.toFXImage (ImageIO.read (new File (P04l)),
				null));
		pToken.add (SwingFXUtils.toFXImage (ImageIO.read (new File (P05l)),
				null));
		pToken.add (SwingFXUtils.toFXImage (ImageIO.read (new File (P06l)),
				null));
		pToken.add (SwingFXUtils.toFXImage (ImageIO.read (new File (P07l)),
				null));
		pToken.add (SwingFXUtils.toFXImage (ImageIO.read (new File (P08l)),
				null));
		pToken.add (SwingFXUtils.toFXImage (ImageIO.read (new File (P10l)),
				null));
		pToken.add (SwingFXUtils.toFXImage (ImageIO.read (new File (P12l)),
				null));

		setPointsTokens (pToken);
	}

	public ArrayList<Participant> readNations() throws IOException {
		ArrayList<Participant> nations = new ArrayList<> ();
		nameMap = new HashMap<> ();

		String nationsFile = resourcesFile + "resources\\Input Information\\ParticipantsFile.txt";
		String flagFile = resourcesFile + "resources\\Nation Info\\Participant Flags\\";
		String diamondFile = resourcesFile + "resources\\Nation Info\\Diamond Flags\\";

		try (BufferedReader bReader = new BufferedReader (new FileReader (
				new File (nationsFile)))) {
			String nation;

			while ((nation = bReader.readLine ()) != null) {
				String[] tokens = nation.split ("\\$");
				Participant newNation = new Participant (tokens[0], tokens[1],
						readImage (flagFile + tokens[0] + ".png"));
				nations.add (newNation);
				newNation.setVotes (voteMap.get (newNation));

				if (readDiamonds) {
					diamondMap.put (newNation, readImage (diamondFile + "Diamond "
							+ newNation.shortName () + ".png"));	
				}
				
				nameMap.put (tokens[0], newNation);

				boolean shouldBeCreated = tokens[8].equals ("P");

				if (shouldBeCreated) {
					participants.add (newNation);
				}
			}
			return nations;
		}
	}

	public void createBanners() throws IOException {
		String nationsFile = resourcesFile + "resources\\Input Information\\ParticipantsFile.txt";

		try (BufferedReader bReader = new BufferedReader (new FileReader (
				new File (nationsFile)))) {
			String nation;

			while ((nation = bReader.readLine ()) != null) {
				String[] tokens = nation.split ("\\$");
				boolean shouldBeCreated = tokens[8].equals ("P");

				if (shouldBeCreated) {
					bannerCreator.createBanners (
							getRosterNationByShortName (tokens[1]), tokens[7],
							tokens[6]);
				}
			}
		}
	}

	public void readEntries() throws NumberFormatException, IOException {
		String mediaFile = resourcesFile + "resources\\Input Information\\ParticipantsFile.txt";
		String mediaLocation = resourcesFile + "resources\\Nation Info\\Entries Videos\\";

		try (BufferedReader bReader = new BufferedReader (new FileReader (
				new File (mediaFile)))) {
			String entry;
			while ((entry = bReader.readLine ()) != null) {
				String[] tokens = entry.split ("\\$");
				Participant p = getRosterNationByShortName (tokens[1]);
				Media entryVid = new Media (new File (mediaLocation + tokens[2]
						+ " - " + tokens[3] + ".mp4").toURI ().toString ());
				int start = Integer.parseInt (tokens[4]);
				int stop = Integer.parseInt (tokens[5]);
				p.setEntry (new Entry (tokens[2], tokens[3], entryVid, start,
						stop));
			}
		}
	}

	private void readVotes() throws FileNotFoundException, IOException {
		String votesFile = resourcesFile + "resources\\Input Information\\VotesFile.txt";
		allVotes = new ArrayList<> ();

		try (BufferedReader bReader = new BufferedReader (new FileReader (
				new File (votesFile)))) {
			String vote;

			while ((vote = bReader.readLine ()) != null) {
				String[] tokens = vote.split ("\\$");
				Votes votes = new Votes (tokens[0], tokens[10].split (" ")[1],
						tokens[9].split (" ")[1], tokens[8].split (" ")[1],
						tokens[7].split (" ")[1], tokens[6].split (" ")[1],
						tokens[5].split (" ")[1], tokens[4].split (" ")[1],
						tokens[3].split (" ")[1], tokens[2].split (" ")[1],
						tokens[1].split (" ")[1], this);

				voteMap.put (getRosterNationByShortName (tokens[0]), votes);
				getRosterNationByShortName (tokens[0]).setVotes (votes);
				allVotes.add (votes);
			}
		}
	}

	public ArrayList<Participant> getAllNations() {
		return new ArrayList<> (nameMap.values ());
	}

	public ArrayList<Participant> getListOfNations(String[] names) {
		ArrayList<Participant> containedNations = new ArrayList<> ();
		for (String name : names) {
			containedNations.add (nameMap.get (name));
		}
		return containedNations;
	}

	public Participant getRosterNationByShortName(String shortName) {
		for (Participant nation : nameMap.values ()) {
			if (nation.shortName ().equals (shortName))
				return nation;
		}
		return null;
	}

	public Participant getRosterNationByFullName(String shortName) {
		for (Participant nation : nameMap.values ()) {
			if (nation.getName ().equals (shortName))
				return nation;
		}
		return null;
	}

	public String[] getListOfNames() {
		String[] namesArray = new String[participants.size ()];
		for (int i = 0; i < participants.size (); i++)
			namesArray[i] = participants.get (i).getName ().get ();
		return namesArray;
	}

	public Votes getVoteByNation(String shortName) {
		return voteMap.get (nameMap.get (shortName));
	}

	public ArrayList<Image> getPointsTokens() {
		return pointsTokens;
	}

	public void setPointsTokens(ArrayList<Image> pointsTokens) {
		this.pointsTokens = pointsTokens;
	}
}
