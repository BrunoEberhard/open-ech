package ch.openech.client.e46;

import ch.openech.client.e10.AddressField;
import ch.openech.client.ewk.event.EchForm;
import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.EchFormats;
import ch.openech.dm.contact.ContactEntry;
import ch.openech.dm.contact.ContactEntryType;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.TextEditField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.toolkit.TextField;

// setTitle("Kontakt");
public class ContactEntryPanel extends EchForm<ContactEntry> {
	
	private final ContactEntryType type;
	private AddressField addressField;

	public ContactEntryPanel(ContactEntryType type, boolean person) {
		super(2);
		this.type = type;

		if (type == ContactEntryType.Phone) {
			line(ContactEntry.CONTACT_ENTRY.phoneCategory);
			line(new OtherCategoryField(ContactEntry.CONTACT_ENTRY.phoneCategoryOther));
		} else {
			line(ContactEntry.CONTACT_ENTRY.categoryCode);
			line(new OtherCategoryField(ContactEntry.CONTACT_ENTRY.categoryOther));
		}
		
		if (type == ContactEntryType.Address) {
			addressField = new AddressField(ContactEntry.CONTACT_ENTRY.address, false, person, !person);
			area(addressField);
		} else {
			line(ContactEntry.CONTACT_ENTRY.value);
		}
		line(ContactEntry.CONTACT_ENTRY.dateFrom, ContactEntry.CONTACT_ENTRY.dateTo);
	}
	
	@Override
	public void fillWithDemoData() {
		if (addressField != null) {
			addressField.setObject(DataGenerator.address(true, true, false));
		}
		super.fillWithDemoData();
	}
	
	private static class OtherCategoryField extends TextEditField implements DependingOnFieldAbove<Object> {

		public OtherCategoryField(String key) {
			super(Constants.getProperty(key), EchFormats.freeKategoryText);
		}

		@Override
		public void valueChanged(Object value) {
			boolean enabled = value == null;
			if (!enabled) {
				setObject(null);
			}
			((TextField) getComponent()).setEnabled(enabled);
		}
		
	}
}
