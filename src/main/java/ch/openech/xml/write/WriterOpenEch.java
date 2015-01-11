package ch.openech.xml.write;

import ch.openech.model.organisation.Organisation;
import ch.openech.model.person.Person;

public class WriterOpenEch extends DeliveryWriter {

	public static final String URI = "http://www.open-ech.ch/xmlns/open-eCH/1";
	public final WriterEch0101 ech101;
	public final WriterEch0046 ech46;
	
	public WriterOpenEch(EchSchema context) {
		super(context);
		
		ech101 = new WriterEch0101(context);
		ech46 = new WriterEch0046(context);
	}
	
	@Override
	protected int getSchemaNumber() {
		return 108;
	}
	
	@Override
	protected String getNamespaceURI() {
		return URI;
	}
	
	public String organisation(Organisation values) throws Exception {
//      WriterElement root = delivery();
//		organisation(root, values);
		delivery();
		return result();
	}
	
	public void openEchPersonExtension(WriterElement parent, Person values) throws Exception {
		openEchPersonExtension(parent, "openEchPersonExtension", values);
	}
	
	public void openEchPersonExtension(WriterElement parent, String tagName, Person person) throws Exception {
		if (!context.extensionAvailable()) return;
		
		boolean hasExtendedInformation = person.personExtendedInformation != null;
		boolean hasContact = !person.contacts.isEmpty();
		
		if (hasExtendedInformation || hasContact) {
			WriterElement element = parent.create(URI, tagName);
			
			if (hasExtendedInformation) {
				person.personExtendedInformation.personIdentification = person.personIdentification(); // hack
				ech101.personExtendedInformation(element, "personExtendedInformation", person.personExtendedInformation);
			}
			if (hasContact) {
				ech46.contact(element, person.contacts);
			}
		}
	}
	
}
