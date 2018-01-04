package ch.openech.frontend.e08;

import static ch.openech.model.common.CountryIdentification.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.TextFormElement;
import org.minimalj.frontend.page.SimpleTableEditorPage;
import org.minimalj.repository.query.By;

import ch.openech.model.common.CountryIdentification;

public class CountryTablePage extends SimpleTableEditorPage<CountryIdentification> {

	public static final Object[] KEYS = new Object[] { $.id, $.countryIdISO2, $.countryNameShort };
	
	public CountryTablePage() {
		super(KEYS);
	}

	@Override
	protected Form<CountryIdentification> createForm(boolean editable, boolean newObject) {
		Form<CountryIdentification> form = new Form<>(editable);
		form.line(!newObject ? new TextFormElement($.id) : $.id);
		form.line($.countryIdISO2);
		form.line($.countryNameShort);
		return form;
	}
	
	@Override
	protected List<CountryIdentification> load() {
		return Backend.find(CountryIdentification.class, By.ALL);
	}
	
}
