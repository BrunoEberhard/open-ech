package ch.openech.frontend.estate;

import static ch.openech.model.estate.Dwelling.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.StringFormElement;
import org.minimalj.frontend.form.element.TextFormElement;
import org.minimalj.frontend.page.SimpleTableEditorPage;
import org.minimalj.repository.query.By;

import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.model.estate.Dwelling;

public class DwellingTablePage extends SimpleTableEditorPage<Dwelling> {

	public static final Object[] COLUMNS = new Object[] { $.EWID, $.floor };

	public DwellingTablePage() {
		super(COLUMNS);
	}

	@Override
	protected Form<Dwelling> createForm(boolean editable, boolean newObject) {
		return new DwellingForm(editable);
	}

	@Override
	protected List<Dwelling> load() {
		return Backend.find(Dwelling.class, By.ALL);
	}

	public static class DwellingForm extends EchForm<Dwelling> {

		public DwellingForm(boolean editable) {
			super(null, editable, 4);
			addTitle("Identifikation");
			line($.EWID);
			line($.administrativeDwellingNo, $.physicalDwellingNo);
			line($.dwellingStatus, new NamedIdFormElement($.localID, editable));
			addTitle("Beschreibung");
			line($.dateOfConstruction, $.dateOfDemolition);
			line($.noOfHabitableRooms, $.floor);
			line($.locationOfDwellingOnFloor, $.multipleFloor);
			line($.surfaceAreaOfDwelling, $.kitchen);
			addTitle("Verwendung");
			line($.dwellingUsage.usageCode, $.dwellingUsage.informationSource);
			// StringFormElement: avoid multi line, remark is not that important
			line($.dwellingUsage.revisionDate, editable ? new StringFormElement($.dwellingUsage.remark, false) : new TextFormElement($.dwellingUsage.remark));
			line($.dwellingUsage.personWithMainResidence, $.dwellingUsage.personWithSecondaryResidence);
			line($.dwellingUsage.dateFirstOccupancy, $.dwellingUsage.dateLastOccupancy);
		}

	}
}
