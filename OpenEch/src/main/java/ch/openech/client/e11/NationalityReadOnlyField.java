package ch.openech.client.e11;

import ch.openech.dm.code.NationalityStatus;
import ch.openech.dm.person.Nationality;
import ch.openech.mj.edit.fields.FormField;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.TextField;

public class NationalityReadOnlyField implements FormField<Nationality> {
	private final PropertyInterface property;
	private final TextField textField;
	
	public NationalityReadOnlyField(PropertyInterface property) {
		this.property = property;
		textField = ClientToolkit.getToolkit().createReadOnlyTextField();
	}

	@Override
	public PropertyInterface getProperty() {
		return property;
	}

	@Override
	public IComponent getComponent() {
		return textField;
	}

	@Override
	public void setObject(Nationality nationality) {
		if (nationality == null) {
			nationality = new Nationality();
			nationality.nationalityStatus = NationalityStatus.unknown;
		}
		display(nationality);
	}
	
	protected void display(Nationality nationality) {
		switch (nationality.nationalityStatus) {
		case with: textField.setText(nationality.nationalityCountry.countryNameShort); break;
		case without: textField.setText("Staatenlos"); break;
		case unknown: textField.setText("Staatsangehörigkeit unbekannt"); break;
		default: textField.setText(""); break;
		}
	}

}
