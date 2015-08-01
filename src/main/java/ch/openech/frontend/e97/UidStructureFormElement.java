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
	public UidStructure parse(String string) {
		UidStructure uid = new UidStructure();
		uid.value = string;
		return uid;
	}

	@Override
	public String render(UidStructure uid) {
		if (uid != null) {
			return uid.value;
		} else {
			return null;
		}
	}

	@Override
	public void mock() {
		UidStructure uid = new UidStructure();
		uid.mock();
		setValue(uid);
	}

}
