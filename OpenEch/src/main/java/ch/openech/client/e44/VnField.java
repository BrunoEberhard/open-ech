package ch.openech.client.e44;

import java.util.List;

import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.toolkit.TextField.TextFieldFilter;
import ch.openech.mj.util.StringUtils;

public class VnField extends AbstractEditField<String> implements DemoEnabled, Validatable {
	private final TextField textField;

	public VnField(String name, boolean editable) {
		super(name, editable);
		if (editable) {
			textField = ClientToolkit.getToolkit().createTextField(listener(), new VnFilter());
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
		textField.setText(generateRandom());
	}

	public static String generateRandom() {
		Long n = 1 + (long)(9999999999L * Math.random());
		return "756" + StringUtils.padLeft(n.toString(), 10, '0');
	}
	
	private class VnFilter implements TextFieldFilter {
		private final int limit = 13;

		@Override
		public String filter(IComponent textField, String str) {
			if (str == null)
				return null;

			for (int i = 0; i<str.length(); i++) {
				if (!Character.isDigit(str.charAt(i))) {
					showBubble(textField, "Es können nur Zahlen eingegeben werden");
					return null;
				}
			}
			
			if (str.length() <= limit) {
				return str;
			} else {
				ClientToolkit.getToolkit().showNotification(textField, "Eingabe auf " + limit + " Zeichen beschränkt");
				return str.substring(0, limit);
			}
		}
	}
	
	@Override
	public void validate(List<ValidationMessage> list) {
		String value = getObject();
		if (StringUtils.isEmpty(value)) return;
		
		long vn = 0;
		try {
			vn = Long.parseLong(value);	
		} catch (NumberFormatException e) {
			// silent
		}
		if (vn < 7560000000001L || vn > 7569999999999L) {
			list.add(new ValidationMessage(getName(), "Die Eingabe muss zw 7560000000001 und 7569999999999 liegen"));
		}
	}

}
