package ch.openech.frontend.e11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.ClientToolkit.Input;
import org.minimalj.frontend.toolkit.ClientToolkit.InputType;
import org.minimalj.model.Rendering.RenderType;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.Codes;
import org.minimalj.util.StringUtils;
import org.minimalj.util.mock.MockName;
import org.minimalj.util.mock.Mocking;

import ch.openech.model.common.CountryIdentification;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.common.Place;

public class PlaceFormElement extends AbstractFormElement<Place> implements Mocking {
	private static final Logger logger = Logger.getLogger(PlaceFormElement.class.getName());

	private final List<CountryIdentification> countries;
	private final Input<CountryIdentification> comboBoxCountry;

	private final List<MunicipalityIdentification> municipalityIdentifications;
	private final Input<String> textFieldMunicipality;
	
	private final IComponent horizontalLayout;

	public PlaceFormElement(PropertyInterface property) {
		super(property);
		countries = Codes.get(CountryIdentification.class);
		municipalityIdentifications = Codes.get(MunicipalityIdentification.class);
		
		comboBoxCountry = ClientToolkit.getToolkit().createComboBox(countries, listener());
		textFieldMunicipality = ClientToolkit.getToolkit().createTextField(100, null, InputType.FREE, munipalityNames(municipalityIdentifications), listener()); // TODO length
		
		horizontalLayout = ClientToolkit.getToolkit().createComponentGroup(comboBoxCountry, textFieldMunicipality);
	}
	
	private List<String> munipalityNames(List<MunicipalityIdentification> municipalityIdentifications) {
		Collections.sort(municipalityIdentifications);
		List<String> names = new ArrayList<>(municipalityIdentifications.size());
		for (MunicipalityIdentification municipalityIdentification : municipalityIdentifications) {
			names.add(municipalityIdentification.render(RenderType.PLAIN_TEXT, Locale.getDefault()));
		}
		return names;
	}

	@Override
	public IComponent getComponent() {
		return horizontalLayout;
	}

	@Override
	public void setValue(Place place) {
		if (place == null) {
			place = new Place();
		}
		if (!place.countryIdentification.isEmpty()) {
			comboBoxCountry.setValue(place.countryIdentification);
		} else {
			comboBoxCountry.setValue(null);
		}
		if (place.municipalityIdentification != null) {
			textFieldMunicipality.setValue(place.municipalityIdentification.municipalityName);
		} else {
			textFieldMunicipality.setValue(null);
		}
	}
	
	@Override
	public Place getValue() {
		Place place = new Place();
		place.countryIdentification = comboBoxCountry.getValue();
		
		CountryIdentification country = comboBoxCountry.getValue();
		if (country != null && !country.isSwiss()) {
			place.municipalityIdentification = null;
			place.foreignTown = textFieldMunicipality.getValue();
		} else {
			String m = textFieldMunicipality.getValue();
			if (!StringUtils.isBlank(m)) {
				m = m.toLowerCase();
				for (MunicipalityIdentification municipality : municipalityIdentifications) {
					if (municipality.municipalityName.toLowerCase().startsWith(m)) {
						place.municipalityIdentification = municipality;
						break;
					}
				}
			} else {
				place.municipalityIdentification = null;
			}
			place.foreignTown = null;
		}
		return place;
	}

	@Override
	public void mock() {
		if (municipalityIdentifications.isEmpty() || countries.isEmpty()) {
			logger.warning("Keine Demodaten für Gemeinden oder Länder vorhanden");
			return;
		}
		Place place = new Place();
		if (Math.random() < 0.8) {
			int index = (int)(Math.random() * municipalityIdentifications.size());
			place.municipalityIdentification = municipalityIdentifications.get(index);
		} else {
			int index = (int)(Math.random() * countries.size());
			place.countryIdentification = countries.get(index);
			place.foreignTown = MockName.officialName() + "Town";
		}
		setValue(place);
	}

}
