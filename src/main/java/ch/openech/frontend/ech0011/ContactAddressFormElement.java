package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.FormLookupFormElement;

import ch.ech.ech0010.MailAddress;

public class ContactAddressFormElement extends FormLookupFormElement<MailAddress> {

	public ContactAddressFormElement(MailAddress key, boolean editable) {
		super(key, editable);
	}

	@Override
	protected Form<MailAddress> createForm() {
		Form<MailAddress> form = new Form<>(Form.EDITABLE);
		form.line(MailAddress.$.addressInformation.street);
		return form;
	}

}
