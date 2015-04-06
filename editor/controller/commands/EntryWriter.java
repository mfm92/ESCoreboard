package controller.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import model.ParticipantData;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
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
		
		PrintStream participantsOut = new PrintStream (new File ("resources/save/participants_" + saveName + ".txt"));
		PrintStream votesOut = new PrintStream (new File ("resources/save/votes_" + saveName + ".txt"));
		PrintStream paramsOut = new PrintStream (new File ("resources/save/params_" + saveName + ".txt"));
		
		final String STRING_SEPARATOR = System.lineSeparator ();
		
		for (ParticipantData p : CoreUI.inputData.getParticipants ()) {
			participantsOut.append (p.getName () + "$" + p.getShortName () + "$" + 
					p.getArtist () + "$" + p.getTitle () + "$" + p.getStart () +
					"$" + p.getStop () + "$" + p.getGrid () + "$" + p.getStatus () + "$" + p.getVoteNr () + STRING_SEPARATOR);
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
		
		HashMap<String, ParticipantData> nameMap = new HashMap<>();
		HashMap<ParticipantData, Integer> scoreMap = new HashMap<>();
		HashMap<ParticipantData, Cell> scoreCellMap = new HashMap<>();
		
		Cell topLeftCell = headerRow.createCell (0);
		topLeftCell.setCellValue (CoreUI.inputData.getNameOfEdition () + " //// " + CoreUI.inputData.getEditionNr ());
		
		ArrayList<ParticipantData> votees = new ArrayList<>();
		ArrayList<ParticipantData> voters = new ArrayList<>();
		FormulaEvaluator evaluator = workBook.getCreationHelper ().createFormulaEvaluator ();
		
		for (ParticipantData pData : CoreUI.inputData.getParticipants ()) {
			switch (pData.getStatus ()) {
				case "P":
					votees.add (pData); voters.add (pData); break;
				case "V":
					voters.add (pData); break;
				case "O":
					votees.add (pData); break;
			}
			
			nameMap.put (pData.getShortName (), pData);
		}

		for (int i = 1; i < 26*26; i++) {
			getColumnDenominator (i);
		}
		
		int counter = 2;
		for (ParticipantData pData : voters) {
			Cell cell = headerRow.createCell (counter++);
			cell.setCellValue (pData.getShortName ());
			
			nameMap.put (pData.getName (), pData);
			
			CellStyle cS = cell.getCellStyle ();
			cS.setRotation ((short) 45);
			cS.setAlignment (CellStyle.ALIGN_CENTER);
			cS.setFillBackgroundColor (HSSFColor.GREY_25_PERCENT.index);
			cell.setCellStyle (cS);
		}
		
		Cell sumHeader = headerRow.createCell (voters.size () + 3);
		sumHeader.setCellValue ("TOTAL PTS");
		
		Cell placeHeader = headerRow.createCell (voters.size () + 4);
		placeHeader.setCellValue ("PLACE");
		
		counter = 2;
		for (ParticipantData pData : votees) {
			Row voterRow = sheet.createRow (counter++);
			Cell nameCell = voterRow.createCell (0);
			nameCell.setCellValue (pData.getName ());
			
			for (int j = 2; j < 2 + voters.size (); ++j) {
				ParticipantData voter = nameMap.get (headerRow.getCell (j).getStringCellValue ());
				Cell cell = voterRow.createCell (j);
				
				if (voter == pData) {
					cell.setCellValue ("X");
					continue;
				}
				
				if (CoreUI.inputData.getVotes ().get (voter) == null) {
					continue;
				}
				
				int counterV = 0;
				for (ParticipantData vData : CoreUI.inputData.getVotes ().get (voter)) {
					if (vData == pData) {
						cell.setCellValue (indicesToPoints (counterV));
					}
					counterV++;
				}
			}
			
			String rowString = getColumnDenominator (voters.size () + 2);
			Cell sumCell = voterRow.createCell ((int) voters.size() + 3);
			Cell placeCell = voterRow.createCell ((int) voters.size () + 4);
			sumCell.setCellFormula ("SUM(C" + counter + ":" + rowString + counter + ")");
			scoreMap.put (pData, (int) evaluator.evaluate (sumCell).getNumberValue ());
			scoreCellMap.put (pData, placeCell);
			
			if (counter > votees.size () + 1) {
				
				ArrayList<ParticipantData> pDatas = votees;
				
				Collections.sort (pDatas, (p1, p2) -> scoreMap.get (p2).compareTo (scoreMap.get (p1)));

				int counterS = 1;
				
				for (ParticipantData pDataInner : pDatas) {
					scoreCellMap.get (pDataInner).setCellValue (counterS++);
				}
			}
		}
		
		File os = new File ("spreadsheet/");
		os.mkdirs ();
		File osText = new File ("spreadsheet/spreadsheet_" + 
				FilenameUtils.removeExtension (saveFile.getName ()) + ".xlsx");	
		osText.createNewFile ();
		
		FileOutputStream fos = new FileOutputStream (osText);
		workBook.write (fos);
		fos.close ();
		workBook.close ();
		
	}
	
	private int indicesToPoints(int index) {
		if (index == 0) return 12;
		if (index == 1) return 10;

		else return 10 - index;
	}
	
	private String getColumnDenominator (int nr) {
		
		int nrSave = nr;
		if (nr <= 0) return null;
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < 5; i++) {
			if (nrSave <= 0) break;
			sb.append ((char)('@' + (nrSave % 26 == 0 ? 26 : nrSave % 26)));
			
			boolean div = nrSave % 26 == 0;
			nrSave /= 26;
			if (div) nrSave --;
		}
		
		sb.reverse ();
		return sb.toString ();
	}
}
