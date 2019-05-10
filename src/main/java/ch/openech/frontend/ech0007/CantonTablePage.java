package ch.openech.frontend.ech0007;

import static ch.ech.ech0071.Canton.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.TablePage;
import org.minimalj.repository.query.By;

import ch.ech.ech0071.Canton;

public class CantonTablePage extends TablePage<Canton> {
	
	@Override
	protected Object[] getColumns() {
		return new Object[] { $.id, $.cantonLongName };
	}

	@Override
	protected List<Canton> load() {
		return Backend.find(Canton.class, By.ALL);
	}
	
}
