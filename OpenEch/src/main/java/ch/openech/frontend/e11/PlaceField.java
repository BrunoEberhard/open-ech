package ch.openech.frontend.e11;

import java.util.logging.Logger;

import org.minimalj.autofill.NameGenerator;
import org.minimalj.frontend.edit.fields.AbstractEditField;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ComboBox;
import org.minimalj.frontend.toolkit.HorizontalLayout;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.SwitchComponent;
import org.minimalj.frontend.toolkit.TextField;
import org.minimalj.model.PropertyInterface;
import org.minimalj.util.DemoEnabled;

import ch.openech.model.common.CountryIdentification;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.common.Place;
import ch.openech.xml.read.StaxEch0071;
import ch.openech.xml.read.StaxEch0072;

public class PlaceField extends AbstractEditField<Place> implements DemoEnabled {
	private static final Logger logger = Logger.getLogger(PlaceField.class.getName());

	private final ComboBox<CountryIdentification> comboBoxCountry;

	private final SwitchComponent switchComponentMunicipality;
	private final ComboBox<MunicipalityIdentification> comboBoxMunicipality;
	private final TextField textForeignTown;
	
	private final HorizontalLayout horizontalLayout;

	public PlaceField(PropertyInterface property) {
		super(property, true);

		comboBoxCountry = ClientToolkit.getToolkit().createComboBox(listener());
		comboBoxCountry.setObjects(StaxEch0072.getInstance().getCountryIdentifications());

		comboBoxMunicipality = ClientToolkit.getToolkit().createComboBox(listener());
		comboBoxMunicipality.setObjects(StaxEch0071.getInstance().getMunicipalityIdentifications());

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
		Place place = new Place();
		if (Math.random() < 0.8) {
			int index = (int)(Math.random() * (double)StaxEch0071.getInstance().getMunicipalityIdentifications().size());
			place.municipalityIdentification = StaxEch0071.getInstance().getMunicipalityIdentifications().get(index);
		} else {
			int index = (int)(Math.random() * (double)StaxEch0072.getInstance().getCountryIdentifications().size());
			place.countryIdentification = StaxEch0072.getInstance().getCountryIdentifications().get(index);
			place.foreignTown = NameGenerator.officialName() + "Town";
		}
		setObject(place);
	}

}
