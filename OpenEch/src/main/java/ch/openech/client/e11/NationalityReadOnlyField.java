package ch.openech.client.e11;

import ch.openech.dm.person.Nationality;
import ch.openech.mj.edit.fields.FormField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.util.StringUtils;

public class NationalityReadOnlyField implements FormField<Nationality> {
	private final String name;
	private final TextField textField;
	
	public NationalityReadOnlyField(String name) {
		this.name = name;
		textField = ClientToolkit.getToolkit().createReadOnlyTextField();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IComponent getComponent() {
		return textField;
	}

	@Override
	public void setObject(Nationality nationality) {
		if (nationality == null) {
			nationality = new Nationality();
			nationality.nationalityStatus = "0";
		}
		display(nationality);
	}
	
	protected void display(Nationality nationality) {
		String status = nationality.nationalityStatus;
		if ("2".equals(status)) {
			textField.setText(nationality.nationalityCountry.countryNameShort);
		} else if ("1".equals(status)) {
			textField.setText("Staatenlos");
		} else if ("0".equals(status) || StringUtils.isBlank(status)) {
			textField.setText("Staatsangehörigkeit unbekannt");
		} else {
			textField.setText("");
		}
	}

}
