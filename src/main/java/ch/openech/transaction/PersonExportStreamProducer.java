package ch.openech.transaction;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.minimalj.backend.Persistence;
import org.minimalj.backend.db.DbPersistence;
import org.minimalj.transaction.Role;
import org.minimalj.transaction.StreamProducer;

import ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterElement;

@Role("Superuser")
public class PersonExportStreamProducer extends StreamProducer<Integer> {
	private static final long serialVersionUID = 1L;

	private final String ewkVersion;
	private final boolean complete;
	
	public PersonExportStreamProducer(String ewkVersion, boolean complete, OutputStream outputStream) {
		super(outputStream);
		this.complete = complete;
		this.ewkVersion = ewkVersion;
	}
	
	@Override
	public Integer execute(Persistence persistence) {
		int count = 0;
		try {
			DbPersistence db = (DbPersistence) persistence;
			int maxId = db.execute(Integer.class, "SELECT COUNT(ID) FROM " + db.name(Person.class));
			
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getStream(), "UTF-8");
			
			EchSchema schema = EchSchema.getNamespaceContext(20, ewkVersion);
			WriterEch0020 writer = new WriterEch0020(schema);
			WriterElement delivery = writer.delivery(outputStreamWriter);
			WriterElement baseDelivery = complete ? writer.baseDelivery(delivery, maxId) : writer.keyDelivery(delivery, maxId);

			for (long id = 1; id<=maxId; id++) {
				Person person = persistence.read(Person.class, id);

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
			getStream().close();
		} catch (Exception x) {
			x.printStackTrace();
		}
		return count;
	}

}
