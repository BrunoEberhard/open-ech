package ch.openech.frontend.ech0011;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.Frontend.Search;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.Codes;
import org.minimalj.util.StringUtils;
import org.minimalj.util.mock.Mocking;

import ch.ech.ech0007.CantonAbbreviation;
import ch.ech.ech0007.SwissMunicipality;
import ch.ech.ech0011.GeneralPlace;
import ch.ech.ech0011.Unknown;
import ch.ech.ech0071.Municipality;
import ch.ech.ech0072.CountryInformation;
import ch.openech.datagenerator.MockName;

public class GeneralPlaceFormElement extends AbstractFormElement<GeneralPlace> implements Mocking {
	private static final Logger logger = Logger.getLogger(GeneralPlaceFormElement.class.getName());

	private final List<CountryInformation> countries;
	private final Input<CountryInformation> comboBoxCountry;

	private final List<Municipality> municipalities;
	private final Input<String> textFieldMunicipality;
	
	private final IComponent componentGroup;

	public GeneralPlaceFormElement(PropertyInterface property) {
		super(property);
		countries = Codes.get(CountryInformation.class);
		municipalities = Codes.get(Municipality.class);
		Collections.sort(municipalities);
		
		comboBoxCountry = Frontend.getInstance().createComboBox(countries, listener());
		
		// int municipalityShortNameSize = AnnotationUtil.getSize(Keys.getProperty(Municipality.$.municipalityShortName));
		
		textFieldMunicipality = Frontend.getInstance().createTextField(100, null, new MunicipalitySearch(), listener());
		
		componentGroup = Frontend.getInstance().createComponentGroup(comboBoxCountry, textFieldMunicipality);
	}

	private CountryInformation findCountry(String countryIdISO2) {
		return countries.stream().filter(country -> StringUtils.equals(countryIdISO2, country.iso2Id)).findFirst().orElse(null);
	}
	
	private Municipality findMunicipality(String name) {
		return municipalities.stream().filter(m -> StringUtils.equals(name, m.municipalityShortName, m.municipalityLongName) && m.municipalityAbolitionNumber == null).findFirst().orElse(null);
	}
	
	@Override
	public IComponent getComponent() {
		return componentGroup;
	}

	@Override
	public void setValue(GeneralPlace place) {
		if (place == null) {
			place = new GeneralPlace();
			place.unknown = Unknown._0;
		}
		
		boolean isSwitzerland = place.swissTown != null;
		if (isSwitzerland) {
			comboBoxCountry.setValue(findCountry("CH"));
			textFieldMunicipality.setValue(place.swissTown.municipalityName);
		} else if (place.foreignCountry != null) {
			comboBoxCountry.setValue(findCountry(place.foreignCountry.country.countryIdISO2));
			textFieldMunicipality.setValue(place.foreignCountry.town);
		} else {
			comboBoxCountry.setValue(null);
			textFieldMunicipality.setValue(null);
		}
	}
	
	@Override
	public GeneralPlace getValue() {
		GeneralPlace place = new GeneralPlace();
		
		CountryInformation country = comboBoxCountry.getValue();
		if (country != null) {
			if (!"CH".equals(country.iso2Id)) {
				place.foreignCountry = new GeneralPlace.ForeignCountry();
				place.foreignCountry.country.countryId = country.id;
				place.foreignCountry.country.countryIdISO2 = country.iso2Id;
				place.foreignCountry.country.countryNameShort = country.shortNameDe;
				place.foreignCountry.town = textFieldMunicipality.getValue();
			} else {
				place.swissTown = new SwissMunicipality();
				String m = textFieldMunicipality.getValue();
				Municipality municipality = findMunicipality(m);
				if (municipality != null) {
					place.swissTown.historyMunicipalityId = municipality.getHistoryMunicipalityId();
					place.swissTown.municipalityId = municipality.municipalityId;
					place.swissTown.municipalityName = municipality.municipalityShortName;
					// TODO Nur 1 CantonAbbreviation
					place.swissTown.cantonAbbreviation = CantonAbbreviation.valueOf(municipality.cantonAbbreviation.name());
				}
			}
		} else {
			place.unknown = Unknown._0;
		}
		return place;
	}

	@Override
	public void mock() {
		if (municipalities.isEmpty() || countries.isEmpty()) {
			logger.warning("Keine Demodaten für Gemeinden oder Länder vorhanden");
			return;
		}
		GeneralPlace place = new GeneralPlace();
		if (Math.random() < 0.8) {
			place.swissTown = new SwissMunicipality();
			int index = (int)(Math.random() * municipalities.size());
			Municipality municipality = municipalities.get(index);
			if (municipality != null) {
				place.swissTown.historyMunicipalityId = municipality.getHistoryMunicipalityId();
				place.swissTown.municipalityId = municipality.municipalityId;
				place.swissTown.municipalityName = municipality.municipalityShortName;
				place.swissTown.cantonAbbreviation = CantonAbbreviation.valueOf(municipality.cantonAbbreviation.name());
			}
		} else {
			int index = (int)(Math.random() * countries.size());
			CountryInformation country = countries.get(index);
			place.foreignCountry = new GeneralPlace.ForeignCountry();
			place.foreignCountry.country.countryId = country.id;
			place.foreignCountry.country.countryIdISO2 = country.iso2Id;
			place.foreignCountry.country.countryNameShort = country.shortNameDe;
			place.foreignCountry.town = textFieldMunicipality.getValue();
			place.foreignCountry.town = MockName.officialName() + "Town";
		}
		setValue(place);
	}

	private class MunicipalitySearch implements Search<String> {

		@Override
		public List<String> search(String query) {
			CountryInformation country = comboBoxCountry.getValue();
			if (country != null && !"CH".equals(country.iso2Id) || query == null || query.length() < 1) {
				return Collections.emptyList();
			} else {
				query = query.toLowerCase();
				List<String> names = new ArrayList<>();
				for (Municipality m : municipalities) {
					if (m.municipalityShortName.toLowerCase().startsWith(query) && m.municipalityAbolitionNumber == null) {
						names.add(m.municipalityShortName);
					}
				}
				return names;
			}
		}

	}
}
