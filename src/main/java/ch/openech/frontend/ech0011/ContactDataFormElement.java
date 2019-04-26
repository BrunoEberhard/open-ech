package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.FormElementConstraint;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.properties.PropertyInterface;

import ch.ech.ech0011.ContactData;

public class ContactDataFormElement extends FormLookupFormElement<ContactData> {

	public ContactDataFormElement(PropertyInterface property) {
		super(property, Form.EDITABLE);
		height(2, FormElementConstraint.MAX);
	}

	public ContactDataFormElement(ContactData key) {
		super(key, Form.EDITABLE);
		height(2, FormElementConstraint.MAX);
	}

	@Override
	public Form<ContactData> createForm() {
		Form<ContactData> form = new Form<>(2);
		form.line(new ContactIdentificationFormElement(ContactData.$.identification));
		form.line(new ContactAddressFormElement(ContactData.$.contactAddress, true));

		// wenn die Referenz geändert wird wird auch automatisch die Kontaktadresse
		// gesetzt
		// form.addDependecy(ContactData.$.getReference(),
		// ContactData.$.contactAddress);

		form.line(ContactData.$.contactValidFrom, ContactData.$.contactValidTill);
		return form;
	}

}
