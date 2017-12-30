package ch.openech.frontend.estate;

import java.util.List;

import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;

import ch.openech.frontend.e44.NamedIdPanel;
import ch.openech.model.common.NamedId;

// merge with TechnicalIdsFormElement ?
class NamedIdFormElement extends ListFormElement<NamedId> {

	public NamedIdFormElement(List<NamedId> key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}

	@Override
	protected void showEntry(NamedId entry) {
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
	protected Form<NamedId> createForm(boolean edit) {
		return new NamedIdPanel();
	}

}