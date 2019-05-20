package ch.openech.frontend.ech0008;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractLookupFormElement.LookupParser;
import org.minimalj.frontend.form.element.SmallListFormElement;
import org.minimalj.model.ViewUtil;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.Codes;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;
import org.minimalj.util.mock.Mocking;

import ch.ech.ech0008.Country;
import ch.ech.ech0011.NationalityData.CountryInfo;
import ch.ech.ech0072.CountryInformation;

public class CountryInfoFormElement extends SmallListFormElement<CountryInfo> implements LookupParser, Mocking {

	public CountryInfoFormElement(List<CountryInfo> key, boolean editable) {
		super(key, editable);
	}

	@Override
	protected String render(List<CountryInfo> infos) {
		StringBuilder s = new StringBuilder();
		if (infos != null) {
			for (CountryInfo info : infos) {
				if (s.length() > 0) {
					s.append(", ");
				}
				s.append(info.country.shortNameDe);
				if (info.nationalityValidFrom != null) {
					s.append(" ab ").append(DateUtils.format(info.nationalityValidFrom));
				}
			}
		}
		return s.toString();
	}

	@Override
	public void mock() {
		List<CountryInformation> countries = Codes.get(CountryInformation.class);
		List<CountryInfo> countryInfos = new ArrayList<>();
		CountryInfo info = new CountryInfo();
		info.country = new Country();
		ViewUtil.view(countries.get(new Random().nextInt(countries.size())), info.country);
		countryInfos.add(info);
		setValue(countryInfos);
	}

	@Override
	protected Form<CountryInfo> createForm() {
		Form<CountryInfo> form = new Form<>(2);
		form.line(new CountryFormElement(CountryInfo.$.country), CountryInfo.$.nationalityValidFrom);
		return form;
	}

	private Country findCountry(String text) {
		Country country = new Country();
		List<CountryInformation> countries = Codes.get(CountryInformation.class);
		Optional<CountryInformation> c = countries.stream().filter(c2 -> text.equals(c2.shortNameDe)).findFirst();
		if (c.isPresent()) {
			ViewUtil.view(c.get(), country);
		} else {
			country.shortNameDe = InvalidValues.createInvalidString(text);
		}
		return country;
	}

	@Override
	public List<CountryInfo> parse(String text) {
		List<CountryInfo> value = new ArrayList<>();
		if (!StringUtils.isEmpty(text)) {
			String[] parts = text.split(", ");
			for (String part : parts) {
				CountryInfo info = new CountryInfo();
				int pos = part.indexOf(" ab ");
				if (pos > -1) {
					info.nationalityValidFrom = DateUtils.parse(part.substring(pos + 4));
					part = part.substring(0, pos);
				}
				info.country = findCountry(part);
				value.add(info);
			}
		}
		return value;
	}

}
