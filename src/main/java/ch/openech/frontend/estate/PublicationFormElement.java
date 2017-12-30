package ch.openech.frontend.estate;

import java.util.List;

import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;

import ch.openech.model.estate.PlanningPermissionApplication.Publication;

public class PublicationFormElement extends ListFormElement<Publication> {

	public PublicationFormElement(List<Publication> key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}
	
	@Override
	protected void showEntry(Publication entry) {
		if (isEditable()) {
			add(entry, new RemoveEntryAction(entry));
		} else {
			add(entry);
		}
	}
	
	@Override
	protected Action[] getActions() {
		return new Action[] { new AddListEntryEditor() };
	}

	@Override
	protected Form<Publication> createForm(boolean edit) {
		Form<Publication> form = new Form<>(2);
		form.line(Publication.$.officialGazette);
		form.line(Publication.$.publicationDate, Publication.$.publicationTill);
		form.line(Publication.$.publicationText);
		return form;
	}
			
}