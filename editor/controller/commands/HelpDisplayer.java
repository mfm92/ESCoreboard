package controller.commands;

import java.awt.Desktop;
import java.io.File;

public class HelpDisplayer {

	public void execute() {
		File pdfFile = new File (System.getProperty ("user.dir") + "/" + 
				"resources/About.pdf");
		try {
			Desktop.getDesktop ().open (pdfFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
