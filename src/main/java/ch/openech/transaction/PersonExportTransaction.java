package ch.openech.transaction;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.minimalj.backend.Backend;
import org.minimalj.repository.query.By;
import org.minimalj.repository.sql.SqlRepository;
import org.minimalj.transaction.OutputStreamTransaction;
import org.minimalj.transaction.Role;

import ch.openech.OpenEchRoles;
import ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterElement;

@Role(OpenEchRoles.importExport)
public class PersonExportTransaction extends OutputStreamTransaction<Integer> {
	private static final long serialVersionUID = 1L;

	private final String ewkVersion;
	private final boolean complete;
	
	public PersonExportTransaction(String ewkVersion, boolean complete, OutputStream outputStream) {
		super(outputStream);
		this.complete = complete;
		this.ewkVersion = ewkVersion;
	}
	
	@Override
	public Integer execute() {
		int count = 0;
		try {
			SqlRepository db = (SqlRepository) Backend.getInstance().getRepository();
			int maxId = db.execute(Integer.class, "SELECT COUNT(ID) FROM " + db.name(Person.class)); // + " WHERE historized = 0");
			
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getStream(), "UTF-8");
			
			EchSchema schema = EchSchema.getNamespaceContext(20, ewkVersion);
			WriterEch0020 writer = new WriterEch0020(schema);
			WriterElement delivery = writer.delivery(outputStreamWriter);
			WriterElement baseDelivery = complete ? writer.baseDelivery(delivery, maxId) : writer.keyDelivery(delivery, maxId);

			for (Person person : Backend.find(Person.class, By.ALL)) {
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
