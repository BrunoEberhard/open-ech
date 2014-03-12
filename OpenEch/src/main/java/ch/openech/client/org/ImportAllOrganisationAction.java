package ch.openech.client.org;

import java.io.InputStream;

import ch.openech.mj.server.DbService;
import ch.openech.mj.server.Services;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ResourceAction;
import ch.openech.xml.read.StaxEch0148;

public class ImportAllOrganisationAction extends ResourceAction {

	public ImportAllOrganisationAction() {
	}

	@Override
	public void action(IComponent context) {
		InputStream inputStream = ClientToolkit.getToolkit().imprt(context, "Datei wählen");
		if (inputStream != null) {
			imprt(inputStream, context);
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
					StaxEch0148 ech0148 = new StaxEch0148(Services.get(DbService.class));

//					progressMonitor.invokeSetNote("Importiere Organisationen");
					ech0148.process(inputStream, "ImportDatei", null);
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
