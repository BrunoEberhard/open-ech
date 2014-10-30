package ch.openech.frontend.e11;

import java.util.List;
import java.util.logging.Logger;

import org.minimalj.autofill.NameGenerator;
import org.minimalj.frontend.edit.fields.AbstractEditField;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.ComboBox;
import org.minimalj.frontend.toolkit.SwitchComponent;
import org.minimalj.frontend.toolkit.TextField;
import org.minimalj.model.PropertyInterface;
import org.minimalj.util.Codes;
import org.minimalj.util.DemoEnabled;

import ch.openech.model.common.CountryIdentification;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.common.Place;

public class PlaceField extends AbstractEditField<Place> implements DemoEnabled {
	private static final Logger logger = Logger.getLogger(PlaceField.class.getName());

	private final List<CountryIdentification> countries;
	private final ComboBox<CountryIdentification> comboBoxCountry;

	private final List<MunicipalityIdentification> municipalityIdentifications;
	private final SwitchComponent switchComponentMunicipality;
	private final ComboBox<MunicipalityIdentification> comboBoxMunicipality;
	private final TextField textForeignTown;
	
	private final IComponent horizontalLayout;


	public PlaceField(PropertyInterface property) {
		super(property, true);
		countries = Codes.get(CountryIdentification.class);
		municipalityIdentifications = Codes.get(MunicipalityIdentification.class);
		
		comboBoxCountry = ClientToolkit.getToolkit().createComboBox(listener());
		comboBoxCountry.setObjects(countries);

		comboBoxMunicipality = ClientToolkit.getToolkit().createComboBox(listener());
		comboBoxMunicipality.setObjects(municipalityIdentifications);

		textForeignTown = ClientToolkit.getToolkit().createTextField(listener(), 100); // TODO

		switchComponentMunicipality = ClientToolkit.getToolkit().createSwitchComponent(comboBoxMunicipality, textForeignTown);
		switchComponentMunicipality.show(comboBoxMunicipality);

		horizontalLayout = ClientToolkit.getToolkit().createHorizontalLayout(comboBoxCountry, switchComponentMunicipality);
	}

	@Override
	protected void fireChange() {
		CountryIdentification countryIdentification = (CountryIdentification) comboBoxCountry.getSelectedObject();
		if (countryIdentification == null || countryIdentification.isSwiss()) {
			switchComponentMunicipality.show(comboBoxMunicipality);
		} else {
			switchComponentMunicipality.show(textForeignTown);
		}
		super.fireChange();
	}

	@Override
	public IComponent getComponent() {
		return horizontalLayout;
	}

	@Override
	public void setObject(Place place) {
		if (place == null) {
			place = new Place();
		}
		if (!place.countryIdentification.isEmpty()) {
			comboBoxCountry.setSelectedObject(place.countryIdentification);
		} else {
			comboBoxCountry.setSelectedObject(null);
		}
		if (place.isSwiss()) {
			comboBoxMunicipality.setSelectedObject(place.municipalityIdentification);
			switchComponentMunicipality.show(comboBoxMunicipality);
		} else if (place.isForeign()) {
			textForeignTown.setText(place.foreignTown);
			switchComponentMunicipality.show(textForeignTown);
		} else {
			logger.info("Empty Place");
			switchComponentMunicipality.show(comboBoxMunicipality);
			comboBoxMunicipality.setSelectedObject(null);
		}
	}
	
	@Override
	public Place getObject() {
		Place place = new Place();
		place.countryIdentification = (CountryIdentification) comboBoxCountry.getSelectedObject();
		if (switchComponentMunicipality.getShownComponent() == comboBoxMunicipality) {
			if (comboBoxMunicipality.getSelectedObject() != null) {
				place.municipalityIdentification = (MunicipalityIdentification) comboBoxMunicipality.getSelectedObject();
			} else {
				place.municipalityIdentification = null;
			}
			place.foreignTown = null;
		} else {
			place.municipalityIdentification = null;
			place.foreignTown = textForeignTown.getText();
		}
		
		return place;
	}

	@Override
	public void fillWithDemoData() {
		if (municipalityIdentifications.isEmpty() || countries.isEmpty()) {
			logger.warning("Keine Demodaten für Gemeinden oder Länder vorhanden");
			return;
		}
		Place place = new Place();
		if (Math.random() < 0.8) {
			int index = (int)(Math.random() * (double)municipalityIdentifications.size());
			place.municipalityIdentification = municipalityIdentifications.get(index);
		} else {
			int index = (int)(Math.random() * (double)countries.size());
			place.countryIdentification = countries.get(index);
			place.foreignTown = NameGenerator.officialName() + "Town";
		}
		setObject(place);
	}

}
