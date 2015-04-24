package ch.openech.frontend.e97;

import org.minimalj.frontend.form.element.FormatFormElement;
import org.minimalj.model.properties.PropertyInterface;

import ch.openech.model.organisation.UidStructure;

// Todo: Formatierung von UidStructure (Firmennummer, Schema von LocalDate benutzen)
public class UidStructureFormElement extends FormatFormElement<UidStructure> {

	public UidStructureFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	@Override
	protected String getAllowedCharacters(PropertyInterface property) {
		return "ACDHEM0123456789";
	}

	@Override
	protected int getAllowedSize(PropertyInterface property) {
		return UidStructure.LENGTH;
	}

	@Override
	public UidStructure getValue() {
		UidStructure uid = new UidStructure();
		uid.value = textField.getValue();
		return uid;
	}

	@Override
	public void setValue(UidStructure uid) {
		if (uid != null) {
			textField.setValue(uid.value);
		} else {
			textField.setValue(null);
		}
	}

	@Override
	public void mock() {
		UidStructure uid = new UidStructure();
		uid.mock();
		setValue(uid);
	}

}
