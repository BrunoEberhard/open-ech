package ch.openech.frontend.estate;

import static ch.openech.model.estate.Street.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.page.SimpleTableEditorPage;
import org.minimalj.repository.query.By;

import ch.openech.model.estate.Street;

public class StreetTablePage extends SimpleTableEditorPage<Street> {

	public static final Object[] COLUMNS = new Object[] {$.ESID, $.getDescriptions(), $.ESTRID};
	
	public StreetTablePage() {
		super(COLUMNS);
	}
	
	@Override
	protected Form<Street> createForm(boolean editable, boolean newObject) {
		Form<Street> form = new Form<>(editable, 2);
		form.line($.ESID, $.isOfficialDescriptionType);
		form.line($.officialStreetNumber);
		form.line($.localisationKind);
		return form;
	}

	@Override
	protected List<Street> load() {
		return Backend.find(Street.class, By.ALL);
	}
	
}
