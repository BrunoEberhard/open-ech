package ch.openech.frontend.e44;

import org.minimalj.frontend.edit.fields.TextFormatField;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;
import ch.openech.model.person.types.Vn;

// Todo: Formatierung von Vn (AHV Nr, Schema von LocalDate benutzen)
public class VnField extends TextFormatField<Vn> {

	public VnField(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	public VnField(PropertyInterface property) {
		super(property);
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
	public Vn getObject() {
		Vn vn = new Vn();
		vn.value = textField.getText();
		if (!StringUtils.isEmpty(vn.value)) {
			vn.value = vn.value.replace(".", "");
		}
		return vn;
	}

	@Override
	public void setObject(Vn vn) {
		if (vn != null) {
			textField.setText(vn.getFormattedValue());
		} else {
			textField.setText(null);
		}
	}

	@Override
	public void fillWithDemoData() {
		Vn vn = new Vn();
		vn.fillWithDemoData();
		setObject(vn);
	}

}
