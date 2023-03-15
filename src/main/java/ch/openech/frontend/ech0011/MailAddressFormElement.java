package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.FormElementConstraint;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.util.Codes;

import ch.ech.ech0008.Country;
import ch.ech.ech0010.MailAddress;
import ch.openech.frontend.ech0010.AddressForm;

public class MailAddressFormElement extends FormLookupFormElement<MailAddress> {

	public MailAddressFormElement(MailAddress key, boolean editable) {
		super(key, editable);
		height(3, FormElementConstraint.MAX);
	}

	@Override
	protected MailAddress createObject() {
		MailAddress mailAddress = super.createObject();
		if (mailAddress.addressInformation.country == null) {
			mailAddress.addressInformation.country = Codes.get(Country.class, 8100);
		}
		return mailAddress;
	}

	@Override
	protected String render(MailAddress value) {
		// TODO Auto-generated method stub
		return super.render(value);
	}

	@Override
	protected Form<MailAddress> createForm() {
        return new AddressForm(Form.EDITABLE, true, true, false);
	}

}
