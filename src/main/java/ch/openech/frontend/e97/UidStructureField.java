package ch.openech.frontend.e97;

import org.minimalj.frontend.edit.fields.TextFormatField;
import org.minimalj.model.properties.PropertyInterface;

import ch.openech.model.organisation.UidStructure;

// Todo: Formatierung von UidStructure (Firmennummer, Schema von LocalDate benutzen)
public class UidStructureField extends TextFormatField<UidStructure> {

	public UidStructureField(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	public UidStructureField(PropertyInterface property) {
		super(property);
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
	public UidStructure getObject() {
		UidStructure uid = new UidStructure();
		uid.value = textField.getValue();
		return uid;
	}

	@Override
	public void setObject(UidStructure uid) {
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
		setObject(uid);
	}

}
