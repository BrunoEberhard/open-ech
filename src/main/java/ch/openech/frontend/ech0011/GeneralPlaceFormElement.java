package ch.openech.frontend.ech0011;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.Frontend.Search;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.action.ActionGroup;
import org.minimalj.frontend.editor.Editor.SimpleEditor;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.ViewUtil;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.Codes;
import org.minimalj.util.StringUtils;
import org.minimalj.util.mock.Mocking;

import ch.ech.ech0007.SwissMunicipality;
import ch.ech.ech0008.Country;
import ch.ech.ech0011.GeneralPlace;
import ch.ech.ech0011.GeneralPlace.ForeignCountry;
import ch.ech.ech0011.Unknown;
import ch.ech.ech0071.CantonAbbreviation;
import ch.ech.ech0071.Municipality;
import ch.ech.ech0072.CountryInformation;
import ch.openech.datagenerator.MockName;

// ein Textfeld, bei dem z.b. "Amden SG", "Berlin, Deutschland" oder "unbekannt" eingegeben werden kann
// zusäztlich ein "Frontend.getInstance().createLookup(textField, actions)" damit Schweizer
// oder ausländischer Ort in Formular eingegeben werden kann. Plus Action "Unbekannt"
public class GeneralPlaceFormElement extends AbstractFormElement<GeneralPlace> implements Mocking {
	private static final Logger logger = Logger.getLogger(GeneralPlaceFormElement.class.getName());

	private final List<CountryInformation> countries;
	private final List<Municipality> municipalities;
	private final Input<String> textField;
	private final Input<String> lookup;
	
	public GeneralPlaceFormElement(PropertyInterface property) {
		super(property);
		countries = Codes.get(CountryInformation.class);
		municipalities = Codes.get(Municipality.class).stream().filter(m -> m.municipalityAbolitionMode == null).collect(Collectors.toList());
		Collections.sort(municipalities);
		
		textField = Frontend.getInstance().createTextField(100, null, new MunicipalitySearch(), listener());
		ActionGroup actions = new ActionGroup(null);
		actions.add(new SwissTownAction());
		actions.add(new ForeignCountryAction());
		actions.add(new UnknownAction());
		lookup = Frontend.getInstance().createLookup(textField, actions);
	}

	public GeneralPlaceFormElement(GeneralPlace generalPlace) {
		this(Keys.getProperty(generalPlace));
	}

	private CountryInformation findCountry(String name) {
		return countries.stream().filter(country -> StringUtils.equals(name, country.shortNameDe)).findFirst().orElse(null);
	}
	
	private Municipality findMunicipality(String name) {
		return municipalities.stream().filter(m -> StringUtils.equals(name, m.municipalityShortName, m.municipalityLongName) && m.municipalityAbolitionNumber == null).findFirst().orElse(null);
	}
	
	@Override
	public IComponent getComponent() {
		return lookup;
	}

	private class SwissTownAction extends SimpleEditor<GeneralPlace> {

		@Override
		protected Form<GeneralPlace> createForm() {
			Form<GeneralPlace> form = new Form<>();
			form.line(GeneralPlace.$.swissTown);
			return form;
		}

		@Override
		protected GeneralPlace createObject() {
			GeneralPlace object = GeneralPlaceFormElement.this.getValue();
			GeneralPlace newObject = new GeneralPlace();
			if (object != null) {
				newObject.swissTown = object.swissTown;
			}
			return newObject;
		}

		@Override
		protected GeneralPlace save(GeneralPlace savedObject) {
			GeneralPlaceFormElement.this.setValue(savedObject);
			GeneralPlaceFormElement.this.fireChange();
			return savedObject;
		}
	}

	private class ForeignCountryAction extends SimpleEditor<GeneralPlace> {

		@Override
		protected Form<GeneralPlace> createForm() {
			Form<GeneralPlace> form = new Form<>();
			form.line(GeneralPlace.$.foreignCountry.country);
			form.line(GeneralPlace.$.foreignCountry.town);
			return form;
		}

		@Override
		protected GeneralPlace createObject() {
			GeneralPlace object = GeneralPlaceFormElement.this.getValue();
			GeneralPlace newObject = new GeneralPlace();
			if (object != null && object.foreignCountry != null) {
				newObject.foreignCountry = CloneHelper.clone(object.foreignCountry);
			} else {
				newObject.foreignCountry = new ForeignCountry();
			}
			return newObject;
		}

		@Override
		protected GeneralPlace save(GeneralPlace savedObject) {
			GeneralPlaceFormElement.this.setValue(savedObject);
			GeneralPlaceFormElement.this.fireChange();
			return savedObject;
		}
	}

	private class UnknownAction extends Action {

		@Override
		public void action() {
			GeneralPlace object = new GeneralPlace();
			object.unknown = Unknown._0;
			GeneralPlaceFormElement.this.setValue(object);
			GeneralPlaceFormElement.this.fireChange();
		}
	}

	@Override
	public void setValue(GeneralPlace place) {
		if (place == null) {
			place = new GeneralPlace();
			place.unknown = Unknown._0;
		}
		
		boolean isSwitzerland = place.swissTown != null;
		if (isSwitzerland) {
			textField.setValue(place.swissTown.getMunicipalityName());
		} else if (place.foreignCountry != null) {
			StringBuilder s = new StringBuilder();
			if (!StringUtils.isEmpty(place.foreignCountry.town)) {
				s.append(place.foreignCountry.town);
			}
			if (place.foreignCountry.country != null) {
				if (s.length() > 0) {
					s.append(", ");
				}
				s.append(place.foreignCountry.country.shortNameDe);
			}
			textField.setValue(s.toString());
		} else if (place.unknown == Unknown._0) {
			textField.setValue("Unbekannt");
		} else {
			textField.setValue(null);
		}
	}
	
	@Override
	public GeneralPlace getValue() {
		String text = textField.getValue();
		if (text == null) {
			return null;
		}
		if (StringUtils.equals(text, "Unbekannt")) {
			GeneralPlace place = new GeneralPlace();
			place.unknown = Unknown._0;
			return place;
		}
		Municipality municipality = findMunicipality(text);
		if (municipality != null) {
			GeneralPlace place = new GeneralPlace();
			place.swissTown = new SwissMunicipality();
			place.swissTown.setHistoryMunicipalityId(municipality.getHistoryMunicipalityId());
			place.swissTown.municipalityId = municipality.municipalityId;
			place.swissTown.municipalityShortName = municipality.municipalityShortName;
			place.swissTown.cantonAbbreviation = CantonAbbreviation.valueOf(municipality.cantonAbbreviation.name());
			return place;
		} else {
			String countryString;
			String town;
			int index = text.indexOf(",");
			if (index > -1) {
				countryString = text.substring(text.indexOf(",") + 1).trim();
				town = text.substring(0, index - 1).trim();
			} else {
				countryString = text;
				town = null;
			}
			CountryInformation country = findCountry(countryString);
			if (country != null) {
				GeneralPlace place = new GeneralPlace();
				place.foreignCountry = new GeneralPlace.ForeignCountry();
				place.foreignCountry.country = ViewUtil.view(country, new Country());
				place.foreignCountry.town = town;
				return place;
			}
		}
		return null;
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
				ViewUtil.view(municipality, place.swissTown);
			}
		} else {
			int index = (int)(Math.random() * countries.size());
			CountryInformation country = countries.get(index);
			place.foreignCountry = new GeneralPlace.ForeignCountry();
			ViewUtil.view(country, place.foreignCountry.country);
			place.foreignCountry.town = MockName.officialName() + "Town";
		}
		setValue(place);
	}

	private class MunicipalitySearch implements Search<String> {

		@Override
		public List<String> search(String query) {
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
