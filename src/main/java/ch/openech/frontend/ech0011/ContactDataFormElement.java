package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.properties.PropertyInterface;

import ch.ech.ech0011.ContactData;

public class ContactDataFormElement extends FormLookupFormElement<ContactData> {

	public ContactDataFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	public ContactDataFormElement(ContactData key, boolean editable) {
		super(key, editable);
	}

	@Override
	public Form<ContactData> createForm() {
		Form<ContactData> form = new Form<>(2);
		form.line(new ContactReferenceFormElement(ContactData.$.getReference()));
		form.line(new ContactAddressFormElement(ContactData.$.contactAddress, true));
		form.line(ContactData.$.contactValidFrom, ContactData.$.contactValidTill);
		return form;
	}

}
