package ch.openech.client.e97;

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


public class OrganisationUidField extends AbstractEditField<String> implements DemoEnabled, Validatable {

	private static final int[] mult = {5, 4, 3, 2, 7, 6, 5, 4};
	private TextField textField;
	
	public OrganisationUidField() {
		this(null);
	}
	
	public OrganisationUidField(Object key) {
		super(key, true);
		textField = ClientToolkit.getToolkit().createTextField(listener(), new OrganisationTextFieldFilter());
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

	private class OrganisationTextFieldFilter implements TextFieldFilter {
		private static final int limit = 12;

		@Override
		public String filter(IComponent textField, String str) {
			if (str == null)
				return null;
			
			str = str.toUpperCase();
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
		if (value == null || value.length() < 12) {
			list.add(new ValidationMessage(getName(), "Es sind 3 Buchstaben und 9 Ziffern erforderlich"));
			return;
		}
		String organisationIdCategory = value.substring(0, 3);
		if (!StringUtils.equals(organisationIdCategory, "ADM", "CHE")) {
			list.add(new ValidationMessage(getName(), "Die ersten drei Buchstaben müssen ADM oder CHE lauten"));
		}
		for (int i = 3; i<value.length(); i++) {
			if (!Character.isDigit(value.charAt(i))) {
				list.add(new ValidationMessage(getName(), "Die Eingabe muss ausser den ersten drei Buchstaben aus Ziffern bestehen"));
				return;
			}
		}
		if (!checksum(value)) {
			list.add(new ValidationMessage(getName(), "Checksumme ungültig"));
		}
	};
	
	public static boolean checksum(String value) {
		int sum = 0;
		for (int i = 3; i<value.length()-1; i++) {
			sum += (value.charAt(i) - '0') * mult[i - 3];
		}
		sum = sum % 11;
		return sum == (value.charAt(value.length()-1) - '0');
	}
	
	@Override
	public void fillWithDemoData() {
		String value;
		do {
			value = Math.random() < 5 ? "ADM" : "CHE";
			value += (int)(Math.random() * 900000000 + 100000000);
		} while (!checksum(value));
		setObject(value);
	}
	
}
