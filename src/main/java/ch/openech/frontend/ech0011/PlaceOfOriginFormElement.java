package ch.openech.frontend.ech0011;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.Frontend.Search;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.Codes;
import org.minimalj.util.StringUtils;
import org.minimalj.util.mock.Mocking;

import ch.ech.ech0011.PlaceOfOrigin;
import ch.ech.ech0071.CantonAbbreviation;
import ch.ech.ech0071.Municipality;

public class PlaceOfOriginFormElement extends AbstractFormElement<PlaceOfOrigin> implements Mocking {
	private final List<Municipality> municipalities;
	private final Input<String> textField;

	public PlaceOfOriginFormElement(PlaceOfOrigin key) {
		this(Keys.getProperty(key));
	}
	
	public PlaceOfOriginFormElement(PropertyInterface property) {
		super(property);
		municipalities = Codes.get(Municipality.class);
		Collections.sort(municipalities);
		
		textField = Frontend.getInstance().createTextField(100, null, new MunicipalitySearch(), listener());
	}

	private Municipality findMunicipality(String name) {
		return municipalities.stream().filter(m -> StringUtils.equals(name, m.municipalityShortName, m.municipalityLongName) && m.municipalityAbolitionNumber == null).findFirst().orElse(null);
	}
	
	@Override
	public IComponent getComponent() {
		return textField;
	}

	@Override
	public void setValue(PlaceOfOrigin place) {
		if (place != null) {
			textField.setValue(place.originName);
		} else {
			textField.setValue(null);
		}
	}
	
	@Override
	public PlaceOfOrigin getValue() {
		String name = textField.getValue();
		if (!StringUtils.isEmpty(name)) {
			PlaceOfOrigin place = new PlaceOfOrigin();
			Municipality municipality = findMunicipality(name);
			if (municipality != null) {
				place.originName = name;
				place.placeOfOriginId = municipality.municipalityId;
				place.historyMunicipalityId = municipality.getHistoryMunicipalityId(); // ??
				place.canton = CantonAbbreviation.valueOf(municipality.cantonAbbreviation.name());
			} else {
				place.originName = InvalidValues.createInvalidString(name);
				place.canton = CantonAbbreviation.values()[0];
			}
			return place;
		} else {
			return null;
		}
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

	@Override
	public void mock() {
		PlaceOfOrigin place = new PlaceOfOrigin();
		int index = (int) (Math.random() * municipalities.size());
		Municipality municipality = municipalities.get(index);
		place.originName = municipality.municipalityShortName;
		place.placeOfOriginId = municipality.municipalityId;
		place.historyMunicipalityId = municipality.getHistoryMunicipalityId(); // ??
		place.canton = CantonAbbreviation.valueOf(municipality.cantonAbbreviation.name());
		setValue(place);
	}
}
