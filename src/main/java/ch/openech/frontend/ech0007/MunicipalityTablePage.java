package ch.openech.frontend.ech0007;

import static ch.ech.ech0071.Municipality.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.TablePage;
import org.minimalj.repository.query.By;

import ch.ech.ech0071.Municipality;

public class MunicipalityTablePage extends TablePage<Municipality> {

	@Override
	protected Object[] getColumns() {
		return new Object[] { $.id, $.municipalityShortName, $.cantonAbbreviation };
	}
	
	@Override
	protected List<Municipality> load() {
		return Backend.find(Municipality.class, By.ALL);
	}
	
}
