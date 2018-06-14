package ch.openech.frontend.ech0008;

import static ch.ech.ech0072.v1.Country.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.TextFormElement;
import org.minimalj.frontend.page.SimpleTableEditorPage;
import org.minimalj.repository.query.By;

import ch.ech.ech0072.v1.Country;

public class CountryTablePage extends SimpleTableEditorPage<Country> {

	public static final Object[] KEYS = new Object[] { $.id, $.iso2Id, $.getShortName() };
	
	public CountryTablePage() {
		super(KEYS);
	}

	@Override
	protected Form<Country> createForm(boolean editable, boolean newObject) {
		Form<Country> form = new Form<>(editable);
		form.line(!newObject ? new TextFormElement($.id) : $.id);
		form.line($.iso2Id);
		return form;
	}
	
	@Override
	protected List<Country> load() {
		return Backend.find(Country.class, By.ALL);
	}
	
}
