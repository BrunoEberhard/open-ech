package ch.openech.client.e46;

import ch.openech.client.e10.AddressField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.EchFormats;
import ch.openech.dm.contact.ContactEntry;
import ch.openech.dm.contact.ContactEntryType;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.TextEditField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.toolkit.TextField;

// setTitle("Kontakt");
public class ContactEntryPanel extends EchFormPanel<ContactEntry> {
	
//	private final ContactEntryType type;
	private AddressField addressField;

	public ContactEntryPanel(ContactEntryType type, boolean person) {
		super(2);
//		this.type = type;

		Enum<?> code = type != ContactEntryType.Phone ? ContactEntry.CONTACT_ENTRY.categoryCode : ContactEntry.CONTACT_ENTRY.phoneCategory;
		line(code);
		line(new OtherCategoryField(ContactEntry.CONTACT_ENTRY.categoryOther, code));
		
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

		private final Enum<?> keyCategoryCode;
		
		public OtherCategoryField(String key, Enum<?> keyCategoryCode) {
			super(Constants.getProperty(key), EchFormats.freeKategoryText);
			this.keyCategoryCode = keyCategoryCode;
		}

		@Override
		public Object getKeyOfDependedField() {
			return keyCategoryCode;
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
