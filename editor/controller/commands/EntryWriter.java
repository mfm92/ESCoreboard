package controller.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;

import model.ParticipantData;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import controller.CoreUI;

public class EntryWriter {
	
	public void writeOut (File saveFile) throws IOException {
		if (saveFile == null) return;
		
		writeOutSaves (saveFile);
		writeSpreadSheet (saveFile);
	}
	
	private void writeOutSaves (File saveFile) throws IOException {
		String saveName = FilenameUtils.removeExtension (saveFile.getName ());
		
		if (saveFile.exists ()) saveFile.delete ();
		FileWriter saveOut = new FileWriter (saveFile);
		saveOut.write (saveName);
		saveOut.close ();
		
		PrintStream participantsOut = new PrintStream (new File 
				(System.getProperty("user.dir") + "/resources/save/participants_" + saveName + ".txt"));
		PrintStream votesOut = new PrintStream (new File 
				(System.getProperty("user.dir") + "/resources/save/votes_" + saveName + ".txt"));
		PrintStream paramsOut = new PrintStream (new File 
				(System.getProperty("user.dir") + "/resources/save/params_" + saveName + ".txt"));
		
		final String STRING_SEPARATOR = System.lineSeparator ();
		
		for (ParticipantData p : CoreUI.inputData.getParticipants ()) {
			participantsOut.append (p.getName () + "$" + p.getShortName () + "$" + 
					p.getArtist () + "$" + p.getTitle () + "$" + p.getStart () +
					"$" + p.getStop () + "$" + p.getGrid () + "$" + p.getStatus () + STRING_SEPARATOR);
		}
		
		for (Map.Entry<ParticipantData, ArrayList<ParticipantData>> vote : CoreUI.inputData.getVotes ().entrySet ()) {
			votesOut.append (vote.getKey ().getShortName () + "$"
					+ "12 " + vote.getValue ().get (0).getShortName () + "$"
					+ "10 " + vote.getValue ().get (1).getShortName () + "$"
					+ "08 " + vote.getValue ().get (2).getShortName () + "$"
					+ "07 " + vote.getValue ().get (3).getShortName () + "$"
					+ "06 " + vote.getValue ().get (4).getShortName () + "$"
					+ "05 " + vote.getValue ().get (5).getShortName () + "$"
					+ "04 " + vote.getValue ().get (6).getShortName () + "$"
					+ "03 " + vote.getValue ().get (7).getShortName () + "$"
					+ "02 " + vote.getValue ().get (8).getShortName () + "$"
					+ "01 " + vote.getValue ().get (9).getShortName ()
					+ STRING_SEPARATOR);
		}
		
		String flagsDirectory = (CoreUI.inputData.getFlagDirectory () == null || CoreUI.inputData.getFlagDirectory ().equals("null")) ? 
				"null" : CoreUI.inputData.getFlagDirectory ().replace ("\\", "\\\\");
					
		String prettyFlagDirectory = (CoreUI.inputData.getPrettyFlagDirectory () == null || CoreUI.inputData.getPrettyFlagDirectory ().equals ("null")) ?
				"null" : CoreUI.inputData.getPrettyFlagDirectory ().replace ("\\", "\\\\");
					
		String entriesDirectory = (CoreUI.inputData.getEntriesDirectory () == null || CoreUI.inputData.getEntriesDirectory ().equals ("null")) ? 
				"null" : CoreUI.inputData.getEntriesDirectory ().replace ("\\", "\\\\");
		
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

	private void writeSpreadSheet (File saveFile) throws IOException {
		Workbook workBook = new XSSFWorkbook ();
		Sheet sheet = workBook.createSheet();
		
		Row headerRow = sheet.createRow (0);
		
		Cell topLeftCell = headerRow.createCell (0);
		topLeftCell.setCellValue (CoreUI.inputData.getNameOfEdition () + " //// " + CoreUI.inputData.getEditionNr ());
		
		int counter = 2;
		for (ParticipantData pData : CoreUI.inputData.getParticipants()) {
			Cell cell = headerRow.createCell (counter++);
			cell.setCellValue (pData.getName ());
			
			CellStyle cS = cell.getCellStyle ();
			cS.setRotation ((short) 45);
			cS.setAlignment (CellStyle.ALIGN_CENTER);
			cS.setFillBackgroundColor (HSSFColor.GREY_25_PERCENT.index);
		}
		
		File os = new File (System.getProperty ("user.dir") + "/spreadsheet/");	
		os.mkdirs ();
		os = new File (System.getProperty ("user.dir") + "/spreadsheet/spreadsheet" + 
				FilenameUtils.removeExtension (saveFile.getName ()) + ".xlsx");	
		os.createNewFile ();
		
		FileOutputStream fos = new FileOutputStream (os);
		workBook.write (fos);
		fos.close ();
		workBook.close ();
	}
}
