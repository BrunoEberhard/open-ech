package ch.openech.frontend.e11;

import org.minimalj.frontend.edit.fields.FormField;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.TextField;
import org.minimalj.model.properties.PropertyInterface;

import ch.openech.model.code.NationalityStatus;
import ch.openech.model.person.Nationality;

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
		case with: textField.setValue(nationality.nationalityCountry != null ? nationality.nationalityCountry.countryNameShort : ""); break;
		case without: textField.setValue("Staatenlos"); break;
		case unknown: textField.setValue("Staatsangehörigkeit unbekannt"); break;
		default: textField.setValue(""); break;
		}
	}

}
