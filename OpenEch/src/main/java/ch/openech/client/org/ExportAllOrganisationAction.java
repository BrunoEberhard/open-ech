package ch.openech.client.org;

import static ch.openech.dm.XmlConstants.*;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.server.DbService;
import ch.openech.mj.server.Services;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ExportHandler;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ResourceAction;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0098;
import ch.openech.xml.write.WriterEch0148;
import ch.openech.xml.write.WriterElement;

public class ExportAllOrganisationAction extends ResourceAction implements ExportHandler {
	private EchSchema echNamespaceContext;
	private Object source;
	
	public ExportAllOrganisationAction(EchSchema echNamespaceContext) {
		this.echNamespaceContext = echNamespaceContext;
	}
	
	@Override
	public void action(IComponent context) {
		ClientToolkit.getToolkit().export(context, "text", this);
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
					long maxId = Services.get(DbService.class).getMaxId(Organisation.class);
					
					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
					
					WriterEch0148 writer148 = new WriterEch0148(echNamespaceContext);
					WriterEch0098 writer98 = new WriterEch0098(echNamespaceContext);
					WriterElement delivery = writer148.delivery(outputStreamWriter);
					WriterElement baseDelivery = createBaseDelivery(writer148, delivery);

//					boolean canceled = false;
					int numberOfOrganisations = 0;
					for (long id = 1; id<=maxId; id++) {
						Organisation organisation = Services.get(DbService.class).read(Organisation.class, id);
						if (organisation != null) {
							writeOrganisation(writer98, baseDelivery, organisation);
							numberOfOrganisations++;
						} else {
							// Organisation was deleted
						}
//						if (progressMonitor.isCanceled()) {
//							canceled = true;
//							break;
//						}
//						progress.showProgress(id, maxId);
					}

					writeNumberOfOrganisations(writer148, baseDelivery, numberOfOrganisations);
					writer148.result();
					outputStreamWriter.close();
					outputStream.close();

//					if (!canceled) {
//						progressMonitor.showInformation(maxId + " Organisationen erfolgreich exportiert");
//					} else {
//						progressMonitor.showInformation("Export nach " + nv + " Organisationen abgebrochen");
//					}
//					ClientToolkit.getToolkit().showMessage(parent, maxId + " Organisationen erfolgreich exportiert");
				} catch (Exception x) {
					ClientToolkit.getToolkit().showError(parent, "Export fehlgeschlagen\n\n" + x.getMessage());
				}
			}

		}.start();
	}
	
	/*
	 * Wurde nur herausgelöst, damit es in KeyDeliveryAction überschrieben werden kann
	 */
	protected WriterElement createBaseDelivery(WriterEch0148 writer, WriterElement delivery)
			throws Exception {
		return writer.organisationBaseDelivery(delivery);
	}

	/*
	 * Wurde nur herausgelöst, damit es in KeyDeliveryAction überschrieben werden kann
	 */
	protected void writeOrganisation(WriterEch0098 writer, WriterElement baseDelivery, Organisation organisation) throws Exception {
		writer.reportedOrganisationType(baseDelivery, REPORTED_ORGANISATION, organisation);
	}
	
	/*
	 * Wurde nur herausgelöst, damit es in KeyDeliveryAction überschrieben werden kann
	 */
	protected void writeNumberOfOrganisations(WriterEch0148 writer, WriterElement baseDelivery, int numberOfOrganisations) throws Exception {
		writer.numberOfOrganisations(baseDelivery, numberOfOrganisations);
	}

}
