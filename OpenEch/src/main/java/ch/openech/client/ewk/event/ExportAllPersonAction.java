package ch.openech.client.ewk.event;

import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import ch.openech.dm.person.Person;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ExportHandler;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterElement;

public class ExportAllPersonAction extends ResourceAction implements ExportHandler {
	private EchSchema echNamespaceContext;
	private Object source;
	
	public ExportAllPersonAction(EchSchema echNamespaceContext) {
		this.echNamespaceContext = echNamespaceContext;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		source = e.getSource();
		ClientToolkit.getToolkit().export(e.getSource(), "Export", this);
	}

	@Override
	public void export(OutputStream stream) {
		export(stream, source);
	}

	private void export(final OutputStream outputStream, final Object parent) {
		// final ProgressListener progress = ClientToolkit.getToolkit().showProgress(parent, "Export");

		new Thread() {
//			ProgressListener progress = ClientToolkit.getToolkit().showProgress(parent, "Export");
			
			@Override
			public void run() {
				try {
					int maxId = EchServer.getInstance().getPersistence().person().getMaxId();
					
					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
					
					WriterEch0020 writer = new WriterEch0020(echNamespaceContext);
					WriterElement delivery = writer.delivery(outputStreamWriter);
					WriterElement baseDelivery = createBaseDelivery(writer, delivery, maxId);

//					boolean canceled = false;
					for (int id = 1; id<=maxId; id++) {
						Person person = EchServer.getInstance().getPersistence().person().read(id);
						if (person != null) {
							writePerson(writer, baseDelivery, person);
						} else {
							// Person was deleted
						}
//						if (progressMonitor.isCanceled()) {
//							canceled = true;
//							break;
//						}
//						progress.showProgress(id, maxId);
					}

					writer.endDocument();
					outputStreamWriter.close();
					outputStream.close();

//					if (!canceled) {
//						progressMonitor.showInformation(maxId + " Personen erfolgreich exportiert");
//					} else {
//						progressMonitor.showInformation("Export nach " + nv + " Personen abgebrochen");
//					}
//					ClientToolkit.getToolkit().showMessage(parent, maxId + " Personen erfolgreich exportiert");
				} catch (Exception x) {
					ClientToolkit.getToolkit().showError(parent, "Export fehlgeschlagen\n\n" + x.getMessage());
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
