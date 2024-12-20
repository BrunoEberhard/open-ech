package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.FormElementConstraint;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.properties.Property;
import org.minimalj.util.Codes;

import ch.ech.ech0008.Country;
import ch.ech.ech0010.AddressInformation;
import ch.openech.frontend.ech0010.AddressInformationForm;

public class AddressInformationFormElement extends FormLookupFormElement<AddressInformation> {

	public AddressInformationFormElement(Property property, boolean editable) {
		super(property, editable);
		height(1, FormElementConstraint.MAX);
	}

	public AddressInformationFormElement(AddressInformation key, boolean editable) {
		super(key, editable);
		height(1, FormElementConstraint.MAX);
	}

	@Override
	protected AddressInformation createObject() {
		AddressInformation address = super.createObject();
		if (address.country == null) {
			address.country = Codes.get(Country.class, 8100);
		}
		return address;
	}

	@Override
	protected Form<AddressInformation> createForm() {
        return new AddressInformationForm(Form.EDITABLE, true);
	}

}
