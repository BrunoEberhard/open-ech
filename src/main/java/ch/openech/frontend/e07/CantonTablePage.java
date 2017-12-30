package ch.openech.frontend.e07;

import static ch.openech.model.common.Canton.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.TablePage;
import org.minimalj.repository.query.By;

import ch.openech.model.common.Canton;

public class CantonTablePage extends TablePage<Canton> {

	public static final Object[] KEYS = new Object[] { $.id, $.cantonLongName};
	
	public CantonTablePage() {
		super(KEYS);
	}

	@Override
	protected List<Canton> load() {
		return Backend.find(Canton.class, By.ALL);
	}
	
}
