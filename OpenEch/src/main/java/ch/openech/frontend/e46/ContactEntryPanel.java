package ch.openech.frontend.e46;

import ch.openech.frontend.e10.AddressField;
import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.datagenerator.DataGenerator;
import  ch.openech.model.contact.ContactEntry;
import  ch.openech.model.contact.ContactEntryType;

// setTitle("Kontakt");
public class ContactEntryPanel extends EchForm<ContactEntry> {
	
	private final ContactEntryType type;
	private AddressField addressField;

	public ContactEntryPanel(ContactEntryType type, boolean person) {
		super(2);
		this.type = type;

		if (type == ContactEntryType.Phone) {
			line(ContactEntry.$.phoneCategory);
			line(ContactEntry.$.phoneCategoryOther);
		} else {
			line(ContactEntry.$.categoryCode);
			line(ContactEntry.$.categoryOther);
		}
		
		if (type == ContactEntryType.Address) {
			addressField = new AddressField(ContactEntry.$.address, false, person, !person);
			line(addressField);
		} else {
			line(ContactEntry.$.value);
		}
		line(ContactEntry.$.dateFrom, ContactEntry.$.dateTo);
	}
	
	@Override
	protected void fillWithDemoData(ContactEntry contactEntry) {
		super.fillWithDemoData(contactEntry);
		if (addressField != null) {	
			contactEntry.address = DataGenerator.address(true, true, false);
		}
	}

}
