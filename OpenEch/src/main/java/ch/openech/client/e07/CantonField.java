package ch.openech.client.e07;

import java.util.List;

import ch.openech.dm.common.Canton;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
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
		super(key);
		
		if (editable) {
			textField = ClientToolkit.getToolkit().createTextField(listener(), new CantonTextFieldFilter()); // new IndicatingTextField(new LimitedDocument());
		} else {
			textField = ClientToolkit.getToolkit().createReadOnlyTextField();
		}
	}
	
	@Override
	public IComponent getComponent0() {
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
		private static final int limit = 2;

		@Override
		public String filter(IComponent textField, String str) {
			if (str == null)
				return null;

			for (int i = 0; i<str.length(); i++) {
				if (!Character.isLetter(str.charAt(i))) {
					showBubble(textField, "Es können nur Buchstaben eingegeben werden");
					return null;
				}
			}
			str = str.toUpperCase();
			
			if (str.length() <= limit) {
				return str;
			} else {
				showBubble(textField, "Eingabe auf " + limit + " Zeichen beschränkt");
				return str.substring(0, limit);
			}
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
