package ch.openech.frontend.ech0008;

import java.util.List;
import java.util.logging.Logger;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;
import org.minimalj.util.DateUtils;
import org.minimalj.util.mock.Mocking;

import ch.ech.ech0011.NationalityData.CountryInfo;


public class CountryInfoFormElement extends ListFormElement<CountryInfo> implements Mocking {
	private static final Logger logger = Logger.getLogger(CountryInfoFormElement.class.getName());

	public CountryInfoFormElement(List<CountryInfo> key) {
		super(Keys.getProperty(key));
	}

	@Override
	protected void showEntry(CountryInfo entry) {
		String s = entry.country.countryNameShort;
		if (entry.nationalityValidFrom != null) {
			s = s + " ab " + DateUtils.format(entry.nationalityValidFrom);
		}
		if (isEditable()) {
			add(s, new RemoveEntryAction(entry));
		} else {
			add(s);
		}
	}

	@Override
	public void mock() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Form<CountryInfo> createForm(boolean edit) {
		Form<CountryInfo> form = new Form<>(edit, 2);
		form.line(new CountryFormElement(CountryInfo.$.country), CountryInfo.$.nationalityValidFrom);
		return form;
	}


}
