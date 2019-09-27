package ch.openech.frontend.form;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.FormElement;
import org.minimalj.frontend.form.element.TextFormElement;
import org.minimalj.model.properties.PropertyInterface;

import ch.ech.ech0007.SwissMunicipality;
import ch.ech.ech0010.AddressInformation;
import ch.ech.ech0011.ContactData;
import ch.ech.ech0011.ForeignerName;
import ch.ech.ech0011.GeneralPlace;
import ch.ech.ech0021.NameOfParent;
import ch.openech.frontend.ech0007.SwissMunicipalityFormElement;
import ch.openech.frontend.ech0011.AddressInformationFormElement;
import ch.openech.frontend.ech0011.ContactDataFormElement;
import ch.openech.frontend.ech0011.GeneralPlaceFormElement;
import ch.openech.model.DatePartiallyKnown;


public class EchForm<T> extends Form<T> {

	public EchForm(boolean editable, int columns) {
		super(editable, columns);
	}
	
	//

	@Override
	public FormElement<?> createElement(PropertyInterface property, boolean editable) {
		Class<?> type = property.getClazz();
		if (type == DatePartiallyKnown.class) {
			return new DatePartiallyKnownFormElement(property, editable);
		} else if (type == ForeignerName.class) {
			return new ch.openech.frontend.ech0011.ForeignerNameFormElement(property, editable);
		} else if (type == NameOfParent.class) {
			return new ch.openech.frontend.ech0011.NameOfParentFormElement(property, editable);
		} else if (type == GeneralPlace.class) {
			return editable ? new GeneralPlaceFormElement(property) : new TextFormElement(property);
		} else if (type == SwissMunicipality.class) {
			return new SwissMunicipalityFormElement(property);
		} else if (type == ContactData.class) {
			return editable ? new ContactDataFormElement(property) : new TextFormElement(property);
		} else if (type == AddressInformation.class) {
			return new AddressInformationFormElement(property, editable);
		}
		

		return super.createElement(property, editable);
	}

}
