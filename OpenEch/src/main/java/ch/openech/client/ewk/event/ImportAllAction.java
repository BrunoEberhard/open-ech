package ch.openech.client.ewk.event;

import java.awt.event.ActionEvent;
import java.io.InputStream;

import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.server.EchServer;
import ch.openech.xml.read.StaxEch0020;

public class ImportAllAction extends ResourceAction {

	public ImportAllAction() {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		InputStream inputStream = ClientToolkit.getToolkit().imprt(e.getSource(), "Datei wählen");
		if (inputStream != null) {
			imprt(inputStream, e.getSource());
		}
	}
	
	public void imprt(final InputStream inputStream, final Object parent) {
//		final ProgressListener progress = ClientToolkit.getToolkit().showProgress(parent, "Import");
		
		new Thread() {
			@Override
			public void run() {
				try {
					while (inputStream.available() == 0) {
						Thread.sleep(1000);
					}
					StaxEch0020 ech0020 = new StaxEch0020(EchServer.getInstance().getPersistence());

//					progressMonitor.invokeSetNote("Importiere Personen");
					ech0020.process(inputStream, "ImportDatei", null);
					ClientToolkit.getToolkit().showMessage(parent, "Import erfolgreich abgeschlossen");
					inputStream.close();
				} catch (IllegalStateException ise) {
					// silent, probably CloseablePipedInputStream was closed
				} catch (Exception x) {
					ClientToolkit.getToolkit().showError(parent, "Import fehlgeschlagen\n\n" + x.getMessage());
					x.printStackTrace();
				} finally {
					// TODO refresh über Application, nicht über FrameManager
					// FrameManager.getInstance().refresh();
				}
			}
		}.start();
	}
	
}
