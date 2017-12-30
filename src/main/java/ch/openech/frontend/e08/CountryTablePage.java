package ch.openech.frontend.e08;

import static ch.openech.model.common.CountryIdentification.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.TablePage;
import org.minimalj.repository.query.By;

import ch.openech.model.common.CountryIdentification;

public class CountryTablePage extends TablePage<CountryIdentification> {

	public static final Object[] KEYS = new Object[] { $.id, $.countryIdISO2, $.countryNameShort };
	
	public CountryTablePage() {
		super(KEYS);
	}

	@Override
	protected List<CountryIdentification> load() {
		return Backend.find(CountryIdentification.class, By.ALL);
	}
	
}
