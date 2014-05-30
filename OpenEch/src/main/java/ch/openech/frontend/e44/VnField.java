package ch.openech.frontend.e44;

import org.minimalj.frontend.edit.fields.TextFormatField;
import org.minimalj.model.PropertyInterface;
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
			if (vn.value != null && vn.value.length() == 13) {
				textField.setText(vn.value.substring(0,3) + "." + vn.value.substring(3,7) + "." + vn.value.substring(7,11) + "." + vn.value.substring(11,13));
			} else {
				textField.setText(vn.value);
			}
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
