package ch.openech.frontend.e44;

import org.minimalj.frontend.form.element.FormatFormElement;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;
import ch.openech.model.person.types.Vn;

// Todo: Formatierung von Vn (AHV Nr, Schema von LocalDate benutzen)
public class VnFormElement extends FormatFormElement<Vn> {

	public VnFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	@Override
	protected String getAllowedCharacters(PropertyInterface property) {
		return "0123456789.";
	}

	@Override
	protected int getAllowedSize(PropertyInterface property) {
		return EchFormats.vn + 3;
	}

	@Override
	public Vn getValue() {
		Vn vn = new Vn();
		vn.value = textField.getValue();
		if (!StringUtils.isEmpty(vn.value)) {
			vn.value = vn.value.replace(".", "");
		}
		return vn;
	}

	@Override
	public void setValue(Vn vn) {
		if (vn != null) {
			textField.setValue(vn.getFormattedValue());
		} else {
			textField.setValue(null);
		}
	}

	@Override
	public void mock() {
		Vn vn = new Vn();
		vn.mock();
		setValue(vn);
	}

}
