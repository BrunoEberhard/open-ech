package ch.openech.frontend.e07;

import static ch.openech.model.common.MunicipalityIdentification.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.TablePage;
import org.minimalj.repository.query.By;

import ch.openech.model.common.MunicipalityIdentification;

public class MunicipalityTablePage extends TablePage<MunicipalityIdentification> {

	public static final Object[] KEYS = new Object[] { $.id, $.municipalityName, $.canton};
	
	public MunicipalityTablePage() {
		super(KEYS);
	}

	@Override
	protected List<MunicipalityIdentification> load() {
		return Backend.find(MunicipalityIdentification.class, By.ALL);
	}
	
}
