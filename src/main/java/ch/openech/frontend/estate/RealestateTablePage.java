package ch.openech.frontend.estate;

import static ch.openech.model.estate.Realestate.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.page.TablePage.SimpleTablePageWithDetail;
import org.minimalj.repository.query.By;

import ch.openech.model.estate.Realestate;

public class RealestateTablePage extends SimpleTablePageWithDetail<Realestate> {

	public static final Object[] COLUMNS = new Object[] { $.EGRID, $.number };

	public RealestateTablePage() {
		super(COLUMNS);
	}

	@Override
	protected Form<Realestate> createForm(boolean editable) {
		return new RealestateForm(editable);
	}

	@Override
	protected List<Realestate> load() {
		return Backend.find(Realestate.class, By.ALL);
	}

	public static class RealestateForm extends Form<Realestate> {

		public RealestateForm(boolean editable) {
			super(editable, 3);
			addTitle("Identifikation");
			line($.EGRID, $.number, $.numberSuffix);
			line($.realestateType, $.subDistrict, $.lot);
			addTitle("Beschreibung");
			line($.authority, $.date, $.cantonalSubKind);
			line($.status, $.mutnumber, $.identDN);
			line($.squareMeasure, $.realestateIncomplete); // TODO , $.coordinates
			addTitle("Gebäude");
			line(new BuildingFormElement($.building, editable));
			// addTitle("Meta Data");
			// line(new NamedMetaDataFormElement($.namedMetaData, editable));
		}

	}
}
