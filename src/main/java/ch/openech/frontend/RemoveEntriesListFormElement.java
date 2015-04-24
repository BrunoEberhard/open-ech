package ch.openech.frontend;

import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectPanelFormElement;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.model.Keys;

public class RemoveEntriesListFormElement<T> extends ObjectPanelFormElement<List<T>> {

	public RemoveEntriesListFormElement(List<T> key) {
		super(Keys.getProperty(key), true);
	}

	private class RemoveThisObjectAction extends Action {
		private final T object;
		
		private RemoveThisObjectAction(T object) {
			this.object = object;
		}

		@Override
		public void action() {
			getObject().remove(object);
			setObject(getObject());
		}
	}

//	private class AddAllAction extends ResourceAction {
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			setObject(new ArrayList<T>(values));
//		}
//	}

	@Override
	protected Form<List<T>> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void show(List<T> objects) {
		for (T object : objects) {
			addObject(object);
			addAction(new RemoveThisObjectAction(object));
		}
//		addGap();
//		addAction(new AddAllAction());
	}

}
