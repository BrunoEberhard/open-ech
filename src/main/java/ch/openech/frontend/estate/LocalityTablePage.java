package ch.openech.frontend.estate;

import static ch.openech.model.estate.Locality.$;

import java.util.Arrays;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.TextFormElement;
import org.minimalj.frontend.page.TablePage.SimpleTablePageWithDetail;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.repository.query.By;
import org.minimalj.util.Codes;

import ch.openech.model.estate.Locality;

public class LocalityTablePage extends SimpleTablePageWithDetail<Locality> {

	public static final Object[] COLUMNS = new Object[] {$.getSwissZipCode(), $.getSwissZipCodeAddOn(), $.name.nameLong};
	
	public LocalityTablePage() {
		super(COLUMNS);
	}
	
	public List<Action> getActions() {
		return Arrays.asList(new NewLocalityEditor(), new DetailEditor(), new DeleteDetailAction());
	}
	
	public class NewLocalityEditor extends TablePageEditor {
		
		@Override
		protected Form<Locality> createForm() {
			Form<Locality> form = new Form<>(Form.EDITABLE, 2);
			form.line($.zip, $.zipAddOn);
			form.line($.name.language);
			form.line($.name.nameLong);
			form.line($.name.nameShort);
			return form;
		}
		
		@Override
		protected Locality createObject() {
			return new Locality();
		}
		
		@Override
		protected void validate(Locality locality, List<ValidationMessage> validationMessages) {
			Integer newId = locality.getNewId();
			if (newId != null && Codes.findCode(Locality.class, newId) != null) {
				validationMessages.add(new ValidationMessage($.zip, "PLZ existiert bereits"));
			}
		}
		
		@Override
		protected Locality save(Locality locality) {
			locality.id = locality.getNewId();
			return super.save(locality);
		}
	}
	
	@Override
	protected Form<Locality> createForm(boolean editable) {
		Form<Locality> form = new Form<>(editable, 2);
		form.line(new TextFormElement($.getSwissZipCode()), new TextFormElement($.getSwissZipCodeAddOn()));
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