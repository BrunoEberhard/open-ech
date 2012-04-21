package ch.openech.client.e07;

import java.util.List;

import ch.openech.dm.common.Canton;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.toolkit.TextField.TextFieldFilter;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.read.StaxEch0071;

public class CantonField extends AbstractEditField<String> implements DemoEnabled, Validatable {
	
	private TextField textField;

	public CantonField(Object key) {
		this(key, true);
	}
	
	public CantonField(Object key, boolean editable) {
		super(key, editable);
		
		if (editable) {
			textField = ClientToolkit.getToolkit().createTextField(listener(), new CantonTextFieldFilter()); // new IndicatingTextField(new LimitedDocument());
		} else {
			textField = ClientToolkit.getToolkit().createReadOnlyTextField();
		}
	}
	
	@Override
	public Object getComponent() {
		return textField;
	}

	@Override
	public String getObject() {
		return textField.getText();
	}		
	
	@Override
	public void setObject(String value) {
		textField.setText(value);
	}

	@Override
	public void fillWithDemoData() {
	}

	private static class CantonTextFieldFilter implements TextFieldFilter {
		private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		@Override
		public int getLimit() {
			return 2;
		}

		@Override
		public String getAllowedCharacters() {
			return ALLOWED_CHARACTERS;
		}
	}	

	@Override
	public void validate(List<ValidationMessage> list) {
		String value = getObject();
		if (value == null || value.length() < 2) {
			list.add(new ValidationMessage(getName(), "Es sind mindestens 2 Buchstaben erforderlich"));
			return;
		}
		List<Canton> cantons = StaxEch0071.getInstance().getCantons();
		for (Canton canton : cantons) {
			if (StringUtils.equals(canton.cantonAbbreviation, value)) return;
		}
		list.add(new ValidationMessage(getName(), "Kein gültiger Kanton"));
	}

}
