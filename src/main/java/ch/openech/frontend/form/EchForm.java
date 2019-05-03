package ch.openech.frontend.form;

import java.util.HashMap;
import java.util.Map;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.FormElement;
import org.minimalj.frontend.form.element.TextFormElement;
import org.minimalj.model.properties.PropertyInterface;

import ch.ech.ech0011.ContactData;
import ch.ech.ech0011.ForeignerName;
import ch.ech.ech0011.GeneralPlace;
import ch.ech.ech0021.NameOfParent;
import ch.openech.frontend.ech0011.ContactDataFormElement;
import ch.openech.frontend.ech0011.GeneralPlaceFormElement;
import ch.openech.xml.DatePartiallyKnown;


public class EchForm<T> extends Form<T> {

	private final Map<Class<?>, Class<?>> formElements = new HashMap<>();
	
	public EchForm(boolean editable, int columns) {
		super(editable, columns);

//		addFormElements();
	}
	
//	private Map<Class<?>, Class<?>> addFormElements() {
//		Map<Class<?>, Class<?>> formElements = new HashMap<>();
//		addFormElement(formElements, DatePartiallyKnownFormElement.class);
//		addFormElement(formElements, ForeignerNameFormElement.class);
//		return formElements;
//	}
//	
//	private void addFormElement(Map<Class<?>, Class<?>> map, Class<?> formElement) {
//		Class<?> type = GenericUtils.getGenericClass(formElement);
//		map.put(type, formElement);
//	}
	
	//

	@Override
	public FormElement<?> createElement(PropertyInterface property, boolean editable) {
		Class<?> type = property.getClazz();
//		if (formElements.containsKey(type)) {
//			return CloneHelper.newInstance(formElements.get(type));
//		}
		if (type == DatePartiallyKnown.class) {
			return new DatePartiallyKnownFormElement(property, editable);
		} else if (type == ForeignerName.class) {
			return new ch.openech.frontend.ech0011.ForeignerNameFormElement(property, editable);
		} else if (type == NameOfParent.class) {
			return new ch.openech.frontend.ech0011.NameOfParentFormElement(property, editable);
		} else if (type == GeneralPlace.class) {
			return editable ? new GeneralPlaceFormElement(property) : new TextFormElement(property);
		} else if (type == ContactData.class) {
			return editable ? new ContactDataFormElement(property) : new TextFormElement(property);
		}
		
		return super.createElement(property, editable);
	}

}
