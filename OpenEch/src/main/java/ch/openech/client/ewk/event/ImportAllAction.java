package ch.openech.client.ewk.event;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFileChooser;

import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.swing.FrameManager;
import ch.openech.server.EchServer;
import ch.openech.xml.read.ParserTargetDB3;
import ch.openech.xml.read.StaxEch0020;

public class ImportAllAction extends ResourceAction {

	public ImportAllAction() {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		imprt();
	}
	
	public void imprt() {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (JFileChooser.APPROVE_OPTION == chooser.showDialog(null, "Datei wählen")) {
			File inputFile = chooser.getSelectedFile();
			imprt(inputFile);
		}				
	}
	
	public void imprt(final File inputFile) {
		final ThreadSafeProgressMonitor progressMonitor = new ThreadSafeProgressMonitor(null, "Import Gesamtbestand", "Initialisierung", 0, 100);
		
		new Thread() {
			@Override
			public void run() {
				try {
					ParserTargetDB3 parserTarget = new ParserTargetDB3(EchServer.getInstance().getPersistence());
					StaxEch0020 ech0020 = new StaxEch0020(parserTarget);

					FileInputStream fis = new FileInputStream(inputFile);
					progressMonitor.invokeSetNote("Importiere Personen");
					ech0020.process(fis, inputFile.getName(), progressMonitor);
					progressMonitor.showInformation("Import erfolgreich abgeschlossen");
					fis.close();
				} catch (Exception x) {
					progressMonitor.showInformation("Import fehlgeschlagen\n\n" + x.getMessage());
					x.printStackTrace();
				} finally {
					FrameManager.getInstance().refresh();
				}
			}
		}.start();
	}
	
}
