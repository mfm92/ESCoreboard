package controller.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;

import controller.CoreUI;
import javafx.beans.property.StringProperty;
import model.ParticipantData;

public class EntryWriter {
	
	public void writeOut (File outputFile) throws FileNotFoundException {
		PrintStream participantsOut = new PrintStream (new File (outputFile + "\\participants.txt"));
		PrintStream votesOut = new PrintStream (new File (outputFile + "\\votes.txt"));
		PrintStream paramsOut = new PrintStream (new File (outputFile + "\\params.txt"));
		
		final String STRING_SEPARATOR = System.lineSeparator ();
		
		for (ParticipantData p : CoreUI.inputData.getParticipants ()) {
			participantsOut.append (p.getName () + "$" + p.getShortName () + "$" + 
					p.getArtist () + "$" + p.getTitle () + "$" + p.getStart () +
					"$" + p.getStop () + "$" + p.getGrid () + "$" + p.getStatus () + STRING_SEPARATOR);
		}
		
		for (Map.Entry<ParticipantData, ArrayList<StringProperty>> vote : CoreUI.inputData.getVotes ().entrySet ()) {
			votesOut.append (vote.getKey ().getShortName () + "$"
					+ "12 " + CoreUI.inputData.getShortName (vote.getValue ().get (0).get ()) + "$"
					+ "10 " + CoreUI.inputData.getShortName (vote.getValue ().get (1).get ()) + "$"
					+ "08 " + CoreUI.inputData.getShortName (vote.getValue ().get (2).get ()) + "$"
					+ "07 " + CoreUI.inputData.getShortName (vote.getValue ().get (3).get ()) + "$"
					+ "06 " + CoreUI.inputData.getShortName (vote.getValue ().get (4).get ()) + "$"
					+ "05 " + CoreUI.inputData.getShortName (vote.getValue ().get (5).get ()) + "$"
					+ "04 " + CoreUI.inputData.getShortName (vote.getValue ().get (6).get ()) + "$"
					+ "03 " + CoreUI.inputData.getShortName (vote.getValue ().get (7).get ()) + "$"
					+ "02 " + CoreUI.inputData.getShortName (vote.getValue ().get (8).get ()) + "$"
					+ "01 " + CoreUI.inputData.getShortName (vote.getValue ().get (9).get ())
					+ STRING_SEPARATOR);
		}
		
		String currentDir = outputFile.getAbsolutePath ().replace ("\\", "\\\\");
		String flagsDirectory = CoreUI.inputData.getFlagDirectory ().replace ("\\", "\\\\");
		String prettyFlagDirectory = CoreUI.inputData.getPrettyFlagDirectory ().replace ("\\", "\\\\");
		String entriesDirectory = CoreUI.inputData.getEntriesDirectory ().replace ("\\", "\\\\");
		
		paramsOut.append ("CURRENT_FILE_PATH = " + currentDir + STRING_SEPARATOR + STRING_SEPARATOR);
		paramsOut.append ("NAME_EDITION = " + CoreUI.inputData.getNameOfEdition () + STRING_SEPARATOR);
		paramsOut.append ("EDITION_NR = " + CoreUI.inputData.getEditionNr () + STRING_SEPARATOR + STRING_SEPARATOR);
		paramsOut.append ("FLAGS_DIR = " + flagsDirectory + STRING_SEPARATOR);
		paramsOut.append ("PRETTY_FLAGS_DIR = " + prettyFlagDirectory + STRING_SEPARATOR);
		paramsOut.append ("ENTRIES_DIR = " + entriesDirectory + STRING_SEPARATOR + STRING_SEPARATOR);
		paramsOut.append ("CREATE_BANNERS = " + CoreUI.inputData.getBannerCreatorActivated () + STRING_SEPARATOR);
		paramsOut.append ("USE_FULLSCREEN = " + CoreUI.inputData.getUseFullScreen () + STRING_SEPARATOR);
		paramsOut.append ("USE_PRETTY_FLAGS = " + CoreUI.inputData.getUsePrettyFlags () + STRING_SEPARATOR);
		paramsOut.append ("TRADITIONAL_VOTING = " + CoreUI.inputData.getTraditionalVoting () + STRING_SEPARATOR + STRING_SEPARATOR);
		paramsOut.append ("SPEED_SHOW = " + CoreUI.inputData.getShowSpeed ());
		
		participantsOut.close ();
		votesOut.close ();
		paramsOut.close ();
	}

}
