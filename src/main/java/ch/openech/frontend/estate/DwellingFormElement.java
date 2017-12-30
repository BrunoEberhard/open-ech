package ch.openech.frontend.estate;

import java.util.List;

import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;

import ch.openech.model.estate.Dwelling;

public class DwellingFormElement extends ListFormElement<Dwelling> {

	public DwellingFormElement(List<Dwelling> key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}

	@Override
	protected void showEntry(Dwelling entry) {
		if (isEditable()) {
			add(entry, new ListEntryEditor(entry), new RemoveEntryAction(entry));
		} else {
			add(entry);
		}
	}

	@Override
	protected Action[] getActions() {
		return new Action[] { new AddListEntryEditor() };
	}

	@Override
	protected Form<Dwelling> createForm(boolean edit) {
		return new DwellingTablePage.DwellingForm(edit);
	}
	
}