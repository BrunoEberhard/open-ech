package ch.openech.frontend.ech0008;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.SmallListFormElement;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.Codes;
import org.minimalj.util.DateUtils;
import org.minimalj.util.mock.Mocking;

import ch.ech.ech0008.Country;
import ch.ech.ech0011.NationalityData.CountryInfo;
import ch.ech.ech0072.CountryInformation;

public class CountryInfoFormElement extends SmallListFormElement<CountryInfo> implements Mocking {
	private static final Logger logger = Logger.getLogger(CountryInfoFormElement.class.getName());

	public CountryInfoFormElement(List<CountryInfo> key) {
		super(key, true, true);
	}

	@Override
	protected String render(List<CountryInfo> infos) {
		StringBuilder s = new StringBuilder();
		if (infos != null) {
			for (CountryInfo info : infos) {
				if (s.length() > 0) {
					s.append(", ");
				}
				s.append(info.country.countryNameShort);
				if (info.nationalityValidFrom != null) {
					s.append(" ab ").append(DateUtils.format(info.nationalityValidFrom));
				}
			}
		}
		return s.toString();
	}

	@Override
	public void mock() {
//		List<CountryInformation> countries = Codes.get(CountryInformation.class);
//		CountryInfo info = new CountryInfo();
//		info.country = countries.get(new Random().nextInt(countries.size()));
//		info.nationalityValidFrom
//
	}

	@Override
	protected Form<CountryInfo> createForm() {
		Form<CountryInfo> form = new Form<>(2);
		form.line(new CountryFormElement(CountryInfo.$.country), CountryInfo.$.nationalityValidFrom);
		return form;
	}

	private Country map(CountryInformation countryInformation, Country country) {
		country.countryId = countryInformation.id;
		country.countryIdISO2 = countryInformation.iso2Id;
		country.countryNameShort = countryInformation.shortNameDe;
		return country;
	}

	private void findCountry(String text, Country country) {
		List<CountryInformation> countries = Codes.get(CountryInformation.class);
		Optional<CountryInformation> c = countries.stream().filter(c2 -> text.equals(c2.shortNameDe)).findFirst();
		if (c.isPresent()) {
			map(c.get(), country);
		} else {
			country.countryNameShort = InvalidValues.createInvalidString(text);
		}
	}

	@Override
	protected List<CountryInfo> parse(String text) {
		List<CountryInfo> result = new ArrayList<>();
		if (!text.isEmpty()) {
			String[] parts = text.split(", ");
			for (String part : parts) {
				CountryInfo info = new CountryInfo();
				int pos = part.indexOf(" ab ");
				if (pos > -1) {
					info.nationalityValidFrom = DateUtils.parse(part.substring(pos + 4));
					part = part.substring(0, pos);
				}
				findCountry(part, info.country);
				result.add(info);
			}
		}
		return result;
	}

}
