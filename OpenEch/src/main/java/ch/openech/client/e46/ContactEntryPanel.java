package ch.openech.client.e46;

import ch.openech.client.e10.AddressField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.code.EchCodes;
import ch.openech.dm.contact.ContactEntry;
import ch.openech.mj.db.model.Code;
import ch.openech.mj.edit.fields.DateField;
import ch.openech.mj.edit.fields.FormField;

// setTitle("Kontakt");
public class ContactEntryPanel extends EchFormPanel<ContactEntry> {
	
	private final String type;
	private final boolean person;
	private AddressField addressField;

	public ContactEntryPanel(String type, boolean person) {
		super();
		this.type = type;
		this.person = person;

		addCategoryLine(type);
		addDateLines();
		addValueLine();
	}
	
	@Override
	public FormField<?> createField(Object keyObject) {
		if (ContactEntry.CONTACT_ENTRY.categoryCode.equals(keyObject)) {
			Code code;
			if ("A".equals(type)) code = EchCodes.addressCategory;
			else if ("P".equals(type)) code = EchCodes.phoneCategory;
			else if ("E".equals(type)) code = EchCodes.emailCategory;
			else if ("I".equals(type)) code = EchCodes.internetCategory;
			else throw new IllegalStateException("Type von CategoryEntyPanel nicht gültig");
			
			return createStringField(ContactEntry.CONTACT_ENTRY.categoryCode, code);
		} else {
			return super.createField(keyObject);
		}
	}

	protected void addCategoryLine(String type) {
		line(ContactEntry.CONTACT_ENTRY.categoryCode);
		line(ContactEntry.CONTACT_ENTRY.categoryOther);
	}
	
	protected void addDateLines() {
		line(new DateField(ContactEntry.CONTACT_ENTRY.dateFrom, DateField.NOT_REQUIRED));
		line(new DateField(ContactEntry.CONTACT_ENTRY.dateTo, DateField.NOT_REQUIRED));
	}
	
	protected void addValueLine() {
		if ("A".equals(type)) {
			addressField = new AddressField("address", false, person, !person);
			area(addressField);
		} else {
			line(ContactEntry.CONTACT_ENTRY.value);
		}
	}
	
	@Override
	public void fillWithDemoData() {
		if (addressField != null) {
			addressField.setObject(DataGenerator.address(true, true, false));
		}
		super.fillWithDemoData();
	}
}
