package ch.openech.transaction;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.minimalj.backend.Backend;
import org.minimalj.transaction.StreamProducer;

import ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterElement;

public class PersonExportStreamProducer implements StreamProducer<Integer> {
	private static final long serialVersionUID = 1L;

	private final String ewkVersion;
	private final boolean complete;
	
	public PersonExportStreamProducer(String ewkVersion, boolean complete) {
		this.complete = complete;
		this.ewkVersion = ewkVersion;
	}
	
	@Override
	public Integer consume(Backend backend, OutputStream stream) {
		int count = 0;
		try {
			int maxId = backend.executeStatement(Integer.class, "MaxPerson");
			
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(stream, "UTF-8");
			
			EchSchema schema = EchSchema.getNamespaceContext(20, ewkVersion);
			WriterEch0020 writer = new WriterEch0020(schema);
			WriterElement delivery = writer.delivery(outputStreamWriter);
			WriterElement baseDelivery = complete ? writer.baseDelivery(delivery, maxId) : writer.keyDelivery(delivery, maxId);

			for (long id = 1; id<=maxId; id++) {
				Person person = backend.read(Person.class, id);

				if (person != null) {
					if (complete) {
						writer.eventBaseDelivery(baseDelivery, person);
					} else {
						writer.eventKeyDelivery(baseDelivery, person.personIdentification());
					}
					count++;
				} else {
					// Person was deleted
				}
			}

			writer.endDocument();
			outputStreamWriter.close();
			stream.close();
		} catch (Exception x) {
			x.printStackTrace();
		}
		return count;
	}

}
