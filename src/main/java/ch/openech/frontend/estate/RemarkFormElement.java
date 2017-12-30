package ch.openech.frontend.estate;

import java.util.List;

import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;

import ch.openech.model.estate.PlanningPermissionApplication.Remark;

public class RemarkFormElement extends ListFormElement<Remark> {

	public RemarkFormElement(List<Remark> key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}
	
	@Override
	protected void showEntry(Remark entry) {
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
	protected Form<Remark> createForm(boolean edit) {
		Form<Remark> form = new Form<>();
		form.line(Remark.$.token);
		return form;
	}
			
}