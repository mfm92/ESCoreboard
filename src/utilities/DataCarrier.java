package utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.math.NumberUtils;

import model.ParticipantData;
import nations.Entry;
import nations.Participant;
import nations.Votes;
import bannercreator.BannerCreator;
import bannercreator.SimpleBannerCreator;
import controller.CoreUI;

public class DataCarrier {

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
	
	public Image ptHolder;

	public HashMap<String, Participant> nameMap;
	public HashMap<Participant, Votes> voteMap;
	public HashMap<Participant, Image> diamondMap;

	public ArrayList<Participant> participants = new ArrayList<Participant> ();
	
	private Media dummyMedia;
	private Image dummyFlag;
	private Image dummyPrettyFlag;
	
	boolean readDiamonds;

	BannerCreator bannerCreator = new SimpleBannerCreator (130, 810);

	private ArrayList<Image> pointsTokens;
	public ArrayList<Votes> allVotes = new ArrayList<>();
	
	String resourcesFile = System.getProperty ("user.dir") + "\\";

	public DataCarrier(boolean readDiamonds) throws IOException {
		this.readDiamonds = readDiamonds;
		initialize ();
	}

	public void initialize() throws IOException {
		nameMap = new HashMap<String, Participant> ();
		voteMap = new HashMap<Participant, Votes> ();
		diamondMap = new HashMap<> ();

		readUtilImages ();
		readDummies ();
		readPtsTokens ();
		readNations ();
		readVotes ();
		readEntries ();
		
		if (CoreUI.inputData.getBannerCreatorActivated ()) {
			createBanners ();
		}
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
		ptHolder = readImage (resourcesFile + "Graphics\\SideBarTokens\\PTHolder.png");
	}
	
	private void readDummies () throws IOException {
		String dummyLocation = System.getProperty ("user.dir") + "\\resources\\Nation Info\\"
				+ "Entries Videos\\Dummy.mp4";
		File file = new File (dummyLocation);
		dummyMedia = new Media (file.toURI ().toString ());
		
		dummyFlag = readImage (System.getProperty ("user.dir") + "\\resources\\Nation Info\\"
				+ "Participants Flags\\EmptyEmptyEmpty.png");
		dummyPrettyFlag = dummyFlag;
	}

	private Image readImage(String fileName) throws IOException {
		BufferedImage newBuffImg = null;
		
		File file = new File (fileName);
		if (!file.exists ()) return null;
		
		newBuffImg = ImageIO.read (new File (fileName));
		return SwingFXUtils.toFXImage (newBuffImg, null);
	}

	public void readPtsTokens() throws IOException {
		ArrayList<Image> pToken = new ArrayList<> ();

		String baseLocation = resourcesFile + "resources\\Graphics\\Point Tokens\\";
		
		ArrayList<String> locations = new ArrayList<>();

		locations.add(baseLocation + "1P.png"); 
		locations.add(baseLocation + "2P.png");
		locations.add(baseLocation + "3P.png");
		locations.add(baseLocation + "4P.png");
		locations.add(baseLocation + "5P.png");
		locations.add(baseLocation + "6P.png");
		locations.add(baseLocation + "7P.png");
		locations.add(baseLocation + "8P.png");
		locations.add(baseLocation + "10P.png");
		locations.add(baseLocation + "12P.png");
		
		for (String location : locations) {
			pToken.add (SwingFXUtils.toFXImage (ImageIO.read (new File (location)), null));
		}

		setPointsTokens (pToken);
	}

	public ArrayList<Participant> readNations() throws IOException {
		ArrayList<Participant> nations = new ArrayList<> ();
		nameMap = new HashMap<> ();

		String flagFile = CoreUI.inputData.getFlagDirectory () + "\\";
		String diamondFile = CoreUI.inputData.getPrettyFlagDirectory () + "\\";
		
		for (ParticipantData pData : CoreUI.inputData.getParticipants ()) {
			
			Image flag = readImage (flagFile + pData.getName () + ".png");
			
			Participant newNation = new Participant (pData.getName (), pData.getShortName (), 
					flag == null ? dummyFlag : flag);	
			
			nations.add (newNation);
			newNation.setVotes (voteMap.get (newNation));
			
			if (readDiamonds) {
				Image dFlag = readImage (diamondFile + "Diamond " + newNation.getShortName () + ".png");
				diamondMap.put (newNation, dFlag == null ? dummyPrettyFlag : dFlag);	
			}
			
			nameMap.put (pData.getName (), newNation);
			
			boolean shouldBeCreated = pData.getStatus ().equals ("P");
			
			if (shouldBeCreated) participants.add (newNation);
		}
		
		return nations;
	}

	public void createBanners() throws IOException {		
		for (ParticipantData pData : CoreUI.inputData.getParticipants ()) {
			boolean shouldBeCreated = pData.getStatus ().equals ("P");

			if (shouldBeCreated) {
				bannerCreator.createBanners (getRosterNationByShortName (pData.getShortName ()), 
						pData.getStatus (), pData.getGrid () + "");
			}
		}
	}

	public void readEntries() throws NumberFormatException, IOException {
		String mediaLocation = CoreUI.inputData.getEntriesDirectory () + "\\";
		
		for (ParticipantData pData : CoreUI.inputData.getParticipants ()) {
			Participant p = getRosterNationByShortName (pData.getShortName ());
			
			File mediaFile = new File (mediaLocation + pData.getArtist () + " - " + pData.getTitle () + ".mp4");
			
			if (mediaFile.exists() && NumberUtils.isNumber (pData.getStart ()) && NumberUtils.isNumber (pData.getStop ())) {
				Media entryVid = new Media ((mediaFile).toURI ().toString ());
				p.setEntry (new Entry (pData.getArtist (), pData.getTitle (), entryVid, Integer.parseInt (pData.getStart ()),
					Integer.parseInt (pData.getStop ()), pData.getStatus ()));	
			} else {
				p.setEntry (new Entry (pData.getArtist (), pData.getTitle (), dummyMedia, 13, 33, pData.getStatus ()));
			}
		}
	}

	private void readVotes() throws FileNotFoundException, IOException {
		for (Map.Entry<ParticipantData, ArrayList<ParticipantData>> pair : CoreUI.inputData.getVotes ().entrySet ()) {
			ArrayList<ParticipantData> votees = pair.getValue ();
			
			Votes votes = new Votes (pair.getKey ().getShortName (), 
					votees.get(9).getShortName (),
					votees.get(8).getShortName (),
					votees.get(7).getShortName (),
					votees.get(6).getShortName (),
					votees.get(5).getShortName (),
					votees.get(4).getShortName (),
					votees.get(3).getShortName (),
					votees.get(2).getShortName (),
					votees.get(1).getShortName (),
					votees.get(0).getShortName (),
					this);
			
			voteMap.put (getRosterNationByShortName (pair.getKey ().getShortName ()), votes);
			getRosterNationByShortName (pair.getKey ().getShortName ()).setVotes (votes);
			allVotes.add (votes);
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
			if (nation.getShortName ().equals (shortName))
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
			namesArray[i] = participants.get (i).getName ();
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
