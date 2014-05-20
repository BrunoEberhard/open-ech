package ch.openech.transaction;

import static  ch.openech.model.XmlConstants.*;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.minimalj.backend.Backend;
import org.minimalj.transaction.StreamProducer;

import  ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0097;
import ch.openech.xml.write.WriterEch0098;
import ch.openech.xml.write.WriterEch0148;
import ch.openech.xml.write.WriterElement;

public class OrganisationExportStreamProducer implements StreamProducer<Integer> {
	private static final long serialVersionUID = 1L;

	private final String ewkVersion;
	private final boolean complete;
	
	public OrganisationExportStreamProducer(String ewkVersion, boolean complete) {
		this.complete = complete;
		this.ewkVersion = ewkVersion;
	}
	
	@Override
	public Integer consume(Backend backend, OutputStream stream) {
		int numberOfOrganisations = 0;
		try {
			int maxId = (Integer) backend.executeStatement("MaxOrganisation");
			
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(stream, "UTF-8");
			
			EchSchema schema = EchSchema.getNamespaceContext(20, ewkVersion);
			WriterEch0148 writer148 = new WriterEch0148(schema);
			WriterEch0098 writer98 = new WriterEch0098(schema);
			WriterEch0097 writer97 = new WriterEch0097(schema);
			WriterElement delivery = writer148.delivery(outputStreamWriter);
			WriterElement baseDelivery = complete ? writer148.organisationBaseDelivery(delivery) : writer148.keyExchange(delivery);

			for (long id = 1; id<=maxId; id++) {
				Organisation organisation = backend.read(Organisation.class, id);

				if (organisation != null) {
					if (complete) {
						writer98.reportedOrganisationType(baseDelivery, REPORTED_ORGANISATION, organisation);
					} else {
						writer97.organisationIdentification(baseDelivery, organisation);
					}
					numberOfOrganisations++;
				} else {
					// Organisation was deleted
				}
			}
			if (complete) {
				writer148.numberOfOrganisations(baseDelivery, numberOfOrganisations);
			}
			writer148.result();
			
			outputStreamWriter.close();
			stream.close();
		} catch (Exception x) {
			x.printStackTrace();
		}
		return numberOfOrganisations;
	}

}
