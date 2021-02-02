package ch.openech.frontend;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;

public class RemoveEntriesListFormElement<T> extends ListFormElement<T> {

	public RemoveEntriesListFormElement(List<T> key) {
		super(Keys.getProperty(key), true);
	}

	private class RemoveThisObjectAction extends Action {
		private final T object;
		
		private RemoveThisObjectAction(T object) {
			this.object = object;
		}

		@Override
		public void run() {
			getValue().remove(object);
			setValue(getValue());
		}
	}

//	private class AddAllAction extends ResourceAction {
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			setObject(new ArrayList<T>(values));
//		}
//	}

	@Override
	protected Form<T> createForm() {
		// not used
		return null;
	}
	
	@Override
	protected List<Action> getActions(T entry) {
		return Collections.singletonList(new RemoveThisObjectAction(entry));
	}
}
