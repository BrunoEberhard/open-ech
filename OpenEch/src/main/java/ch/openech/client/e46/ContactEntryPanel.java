package ch.openech.client.e46;

import ch.openech.client.e10.AddressField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.code.EchCodes;
import ch.openech.dm.contact.ContactEntry;
import ch.openech.mj.db.model.Code;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.fields.FormField;
import ch.openech.mj.edit.fields.TextEditField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.toolkit.TextField;

// setTitle("Kontakt");
public class ContactEntryPanel extends EchFormPanel<ContactEntry> {
	
	private final String type;
	private AddressField addressField;

	public ContactEntryPanel(String type, boolean person) {
		super(2);
		this.type = type;

		line(ContactEntry.CONTACT_ENTRY.categoryCode);
		line(new OtherCategoryField(ContactEntry.CONTACT_ENTRY.categoryOther));
		if ("A".equals(type)) {
			addressField = new AddressField("address", false, person, !person);
			area(addressField);
		} else {
			line(ContactEntry.CONTACT_ENTRY.value);
		}
		line(ContactEntry.CONTACT_ENTRY.dateFrom, ContactEntry.CONTACT_ENTRY.dateTo);
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
	
	@Override
	public void fillWithDemoData() {
		if (addressField != null) {
			addressField.setObject(DataGenerator.address(true, true, false));
		}
		super.fillWithDemoData();
	}
	
	private static class OtherCategoryField extends TextEditField implements DependingOnFieldAbove<String> {

		public OtherCategoryField(String name) {
			super(name);
		}

		@Override
		public String getNameOfDependedField() {
			return ContactEntry.CONTACT_ENTRY.categoryCode;
		}

		@Override
		public void setDependedField(EditField<String> field) {
			boolean enabled = field.getObject() == null;
			if (!enabled) {
				setObject(null);
			}
			((TextField) getComponent()).setEnabled(enabled);
		}
		
	}
}
