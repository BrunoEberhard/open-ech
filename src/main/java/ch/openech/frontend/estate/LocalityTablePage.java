package ch.openech.frontend.estate;

import static ch.openech.model.estate.Locality.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.TextFormElement;
import org.minimalj.frontend.page.SimpleTableEditorPage;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.repository.query.By;
import org.minimalj.util.Codes;

import ch.openech.model.estate.Locality;

public class LocalityTablePage extends SimpleTableEditorPage<Locality> {

	public static final Object[] COLUMNS = new Object[] {$.getSwissZipCode(), $.getSwissZipCodeAddOn(), $.name.nameLong};
	
	public LocalityTablePage() {
		super(COLUMNS);
	}
	
	@Override
	protected Locality save(Locality locality) {
		if (locality.id == null) {
			locality.id = locality.getNewId();
		}
		return super.save(locality);
	}
	
	@Override
	protected void validate(Locality locality, boolean newObject, List<ValidationMessage> validationMessages) {
		if (newObject) {
			Integer newId = locality.getNewId();
			if (newId != null && Codes.findCode(Locality.class, newId) != null) {
				validationMessages.add(new ValidationMessage($.zip, "PLZ existiert bereits"));
			}
		}
	}
	
	@Override
	protected Form<Locality> createForm(boolean editable, boolean newObject) {
		Form<Locality> form = new Form<>(editable, 2);
		if (newObject) {
			form.line($.zip, $.zipAddOn);
		} else {
			form.line(new TextFormElement($.getSwissZipCode()), new TextFormElement($.getSwissZipCodeAddOn()));
		}
		form.line($.name.language);
		form.line($.name.nameLong);
		form.line($.name.nameShort);
		return form;
	}

	@Override
	protected List<Locality> load() {
		return Backend.find(Locality.class, By.ALL);
	}
	
}