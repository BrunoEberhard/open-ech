package ch.openech.client.ewk.event;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.swing.JFileChooser;

import ch.openech.dm.person.Person;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterElement;

public class ExportAllAction extends ResourceAction {

	private EchNamespaceContext echNamespaceContext;
	
	public ExportAllAction(EchNamespaceContext echNamespaceContext) {
		this.echNamespaceContext = echNamespaceContext;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (JFileChooser.APPROVE_OPTION == chooser.showDialog(null, "Ausgabedatei wählen")) {
			File outputFile = chooser.getSelectedFile();
			export(outputFile, (Component)e.getSource());
		}
	}

	private void export(final File outputFile, Component parentComponent) {
		final ThreadSafeProgressMonitor progressMonitor = new ThreadSafeProgressMonitor(parentComponent, "Export", "Initialisierung", 0, 100);
		
		new Thread() {
			@Override
			public void run() {
				try {
					int maxId = EchServer.getInstance().getPersistence().person().getMaxId();
					progressMonitor.invokeSetMaximum(maxId);
					
					FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
					
					WriterEch0020 writer = new WriterEch0020(echNamespaceContext);
					WriterElement delivery = writer.delivery(outputStreamWriter);
					WriterElement baseDelivery = createBaseDelivery(writer, delivery, maxId);

					int nv = 0;
					boolean canceled = false;
					for (int id = 1; id<=maxId; id++) {
						progressMonitor.invokeSetNote("Exportiere Person " + id);
						progressMonitor.invokeSetProgress(nv++);
						Person person = EchServer.getInstance().getPersistence().person().read(id);
						if (person != null) {
							writePerson(writer, baseDelivery, person);
						} else {
							// Person was deleted
						}
						if (progressMonitor.isCanceled()) {
							canceled = true;
							break;
						}
					}

					writer.endDocument();
					outputStreamWriter.close();
					fileOutputStream.close();

					if (!canceled) {
						progressMonitor.showInformation(maxId + " Personen erfolgreich exportiert");
					} else {
						progressMonitor.showInformation("Export nach " + nv + " Personen abgebrochen");
					}
				} catch (Exception x) {
					progressMonitor.showInformation("Export fehlgeschlagen\n\n" + x.getMessage());
				}
			}

		}.start();
	}
	
	/*
	 * Wurde nur herausgelöst, damit es in KeyDeliveryAction überschrieben werden kann
	 */
	protected WriterElement createBaseDelivery(WriterEch0020 writer, WriterElement delivery, int maxId)
			throws Exception {
		return writer.baseDelivery(delivery, maxId);
	}

	/*
	 * Wurde nur herausgelöst, damit es in KeyDeliveryAction überschrieben werden kann
	 */
	protected void writePerson(WriterEch0020 writer, WriterElement baseDelivery, Person person) throws Exception {
		writer.eventBaseDelivery(baseDelivery, person);
	}
}
