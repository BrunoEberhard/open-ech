package ch.openech.client.e11;

import java.util.List;
import java.util.logging.Logger;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Place;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.autofill.NameGenerator;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.HorizontalLayout;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
import ch.openech.xml.read.StaxEch0071;
import ch.openech.xml.read.StaxEch0072;

public class PlaceField extends ObjectField<Place> implements DemoEnabled, Validatable {
	private static final Logger logger = Logger.getLogger(PlaceField.class.getName());

	private final ComboBox<CountryIdentification> comboBoxCountry;

	private final SwitchLayout switchLayoutMunicipality;
	private final ComboBox<MunicipalityIdentification> comboBoxMunicipality;
	private final TextField textForeignTown;
	
	private final HorizontalLayout horizontalLayout;

	public PlaceField(Object key) {
		super(key);

		comboBoxCountry = ClientToolkit.getToolkit().createComboBox(listener());
		comboBoxCountry.setObjects(StaxEch0072.getInstance().getCountryIdentifications());

		comboBoxMunicipality = ClientToolkit.getToolkit().createComboBox(listener());
		comboBoxMunicipality.setObjects(StaxEch0071.getInstance().getMunicipalityIdentifications());

		textForeignTown = ClientToolkit.getToolkit().createTextField(listener(), 100); // TODO

		switchLayoutMunicipality = ClientToolkit.getToolkit().createSwitchLayout();
		switchLayoutMunicipality.show(comboBoxMunicipality);

		horizontalLayout = ClientToolkit.getToolkit().createHorizontalLayout(comboBoxCountry, switchLayoutMunicipality);
	}

	@Override
	protected void fireChange() {
		CountryIdentification countryIdentification = (CountryIdentification) comboBoxCountry.getSelectedObject();
		if (countryIdentification == null || countryIdentification.isSwiss()) {
			switchLayoutMunicipality.show(comboBoxMunicipality);
		} else {
			switchLayoutMunicipality.show(textForeignTown);
		}
		super.fireChange();
	}

	@Override
	public Object getComponent() {
		return horizontalLayout;
	}

	// 
	
	@Override
	public void setObject(Place value) {
		if (value == null) {
			value = new Place();
		}
		super.setObject(value);
	}
	
	@Override
	public Place getObject() {
		Place place = super.getObject();
		if (comboBoxCountry.getSelectedObject() != null) {
			((CountryIdentification) comboBoxCountry.getSelectedObject()).copyTo(place.countryIdentification);
		} else {
			place.countryIdentification.clear();
		}
		if (switchLayoutMunicipality.getShownComponent() == comboBoxMunicipality) {
			if (comboBoxMunicipality.getSelectedObject() != null) {
				((MunicipalityIdentification) comboBoxMunicipality.getSelectedObject()).copyTo(place.municipalityIdentification);
			} else {
				place.municipalityIdentification.clear();
			}
			place.foreignTown = null;
		} else {
			place.municipalityIdentification.clear();
			place.foreignTown = textForeignTown.getText();
		}
		
		return place;
	}

	@Override
	public void show(Place value) {
		if (!value.countryIdentification.isEmpty()) {
			comboBoxCountry.setSelectedObject(value.countryIdentification);
		} else {
			comboBoxCountry.setSelectedObject(null);
		}
		if (value.isSwiss()) {
			comboBoxMunicipality.setSelectedObject(value.municipalityIdentification);
			switchLayoutMunicipality.show(comboBoxMunicipality);
		} else if (value.isForeign()) {
			textForeignTown.setText(value.foreignTown);
			switchLayoutMunicipality.show(textForeignTown);
		} else {
			logger.info("Empty Place");
			switchLayoutMunicipality.show(comboBoxMunicipality);
			comboBoxMunicipality.setSelectedObject(null);
		}
	}

	@Override
	public void fillWithDemoData() {
		Place place = new Place();
		if (Math.random() < 0.8) {
			int index = (int)(Math.random() * (double)StaxEch0071.getInstance().getMunicipalityIdentifications().size());
			place.setMunicipalityIdentification(StaxEch0071.getInstance().getMunicipalityIdentifications().get(index));
		} else {
			int index = (int)(Math.random() * (double)StaxEch0072.getInstance().getCountryIdentifications().size());
			place.setCountryIdentification(StaxEch0072.getInstance().getCountryIdentifications().get(index));
			place.foreignTown = NameGenerator.officialName() + "Town";
		}
		setObject(place);
	}

	@Override
	public void validate(List<ValidationMessage> resultList) {
		// Es ist von dem Formular abhängig, ob ein PlaceField leer sein darf. Daher wird z.B.
		// bei BirthChildEvent validiert und nicht hier
	}

}
