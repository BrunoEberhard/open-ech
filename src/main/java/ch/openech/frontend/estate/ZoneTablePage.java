package ch.openech.frontend.estate;


import java.util.ArrayList;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.page.SimpleTableEditorPage;
import org.minimalj.repository.query.By;

import ch.openech.model.estate.PlanningPermissionApplication.Zone;

public class ZoneTablePage extends SimpleTableEditorPage<Zone> {

	public static final Object[] COLUMNS = new Object[] {Zone.$.abbreviatedDesignation, Zone.$.zoneDesignation, Zone.$.zoneType};
	
	public ZoneTablePage() {
		super(COLUMNS);
	}
	
	@Override
	protected SimpleTableEditorPage<Zone>.DetailPage createDetailPage(Zone detail) {
		return NO_DETAIL_PAGE;
	}
	
	@Override
	protected Form<Zone> createForm(boolean editable, boolean newObject) {
		Form<Zone> form = new Form<>(editable);
		form.line(Zone.$.abbreviatedDesignation);
		form.line(Zone.$.zoneDesignation);
		form.line(new SuggestionFormElement(Zone.$.zoneType, zoneTypeList(), editable));
		return form;
	}

	@Override
	protected List<Zone> load() {
		return Backend.find(Zone.class, By.ALL);
	}

	private List<String> zoneTypeList() {
		// sollte wohl noch lokalisiert werden
		List<String> list = new ArrayList<>();
		list.add("Wohnzone");
		list.add("Schutzzone");
		list.add("Gefahrenzone");
		return list;
	}

}
