package controller.commands;

import java.awt.Desktop;
import java.io.File;

public class HelpDisplayer {

	public void execute() {
		File pdfFile = new File (System.getProperty ("user.dir") + File.separator + 
				"resources" + File.separator + "About.pdf");
		try {
			Desktop.getDesktop ().open (pdfFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
