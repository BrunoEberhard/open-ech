package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.*;

import java.util.List;

import org.minimalj.util.StringUtils;

import ch.openech.model.common.NamedId;
import ch.openech.model.contact.Contact;
import ch.openech.model.contact.ContactEntry;

public class WriterEch0046 extends DeliveryWriter {

	public final String URI;
	public final WriterEch0010 ech10;
	public final WriterEch0044 ech44;
	
	public WriterEch0046(EchSchema context) {
		super(context);
		URI = context.getNamespaceURI(46);
		ech10 = new WriterEch0010(context);
		ech44 = new WriterEch0044(context);
	}
	
	@Override
	protected int getSchemaNumber() {
		return 46;
	}
	
	@Override
	protected String getNamespaceURI() {
		return URI;
	}

	@Override
	protected String getRootType() {
		return CONTACT_ROOT;
	}

	public String contactRoot(Contact contact) throws Exception {
		WriterElement contactRoot = delivery();
		contact(contactRoot, contact);
		return result();
	}

	public void contact(WriterElement parent, Contact contact) throws Exception {
		contact(parent, CONTACT, contact);
	}
	
	public void contact(WriterElement element, List<ContactEntry> contacts) throws Exception {
		Contact contact = new Contact();
		contact.entries.addAll(contacts);
		contact(element, CONTACT, contact);
	}
	
	public void contact(WriterElement parent, String tagName, Contact contact) throws Exception {
		if (contact == null) return;
		
		WriterElement contactElement = parent.create(URI, tagName);
		localID(contactElement, contact.stringId);
		for (ContactEntry entry : contact.getAddressList()) address(contactElement, entry);
		for (ContactEntry entry : contact.getEmailList()) email(contactElement, entry);
		for (ContactEntry entry : contact.getPhoneList()) phone(contactElement, entry);
		for (ContactEntry entry : contact.getInternetList()) internet(contactElement, entry);
	}
	
	private void localID(WriterElement parent, String id) throws Exception {
		if (id == null) return;
		NamedId namedPersonId = new NamedId();
		namedPersonId.personIdCategory = "CH.OPENECH";
		namedPersonId.personId = id;
		ech44.namedId(parent, namedPersonId, LOCAL_I_D);
	}

	private void address(WriterElement parent, ContactEntry entry) throws Exception {
		WriterElement element = entryElementWithCategory(parent, entry, ADDRESS);

		ech10.address(element, POSTAL_ADDRESS, entry.address);
		validity(element, entry);
	}

	private void email(WriterElement parent, ContactEntry entry) throws Exception {
		WriterElement element = entryElementWithCategory(parent, entry, EMAIL);

		element.text(EMAIL_ADDRESS, entry.value);
		validity(element, entry);
	}

	private void phone(WriterElement parent, ContactEntry entry) throws Exception {
		WriterElement element = entryElementWithCategory(parent, entry, PHONE);

		element.text(PHONE_NUMBER, entry.value);
		validity(element, entry);
	}

	private void internet(WriterElement parent, ContactEntry entry) throws Exception {
		WriterElement element = entryElementWithCategory(parent, entry, INTERNET);

		element.text(INTERNET_ADDRESS, entry.value);
		validity(element, entry);
	}

	private WriterElement entryElementWithCategory(WriterElement parent, ContactEntry entry, String type) throws Exception {
		WriterElement element = parent.create(URI, type);

		Enum<?> code = PHONE.equals(type) ? entry.phoneCategory : entry.categoryCode;
		if (code != null) {
			element.text(type + "Category", entry.categoryCode);
		}
		if (!StringUtils.isBlank(entry.categoryOther)) {
			element.text("other" + firstLetterToUpper(type) + "Category", entry.categoryOther);
		}
		if (!StringUtils.isBlank(entry.phoneCategoryOther)) {
			element.text("other" + firstLetterToUpper(type) + "Category", entry.phoneCategoryOther);
		}
		return element;
	}

	private String firstLetterToUpper(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	private void validity(WriterElement parent, ContactEntry entry) throws Exception {
		WriterElement element = parent.create(URI, VALIDITY);
		element.values(entry, DATE_FROM, DATE_TO);
	}

}