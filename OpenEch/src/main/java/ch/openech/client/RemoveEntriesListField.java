package ch.openech.client;

import java.util.List;

import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.Keys;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ResourceAction;

public class RemoveEntriesListField<T> extends ObjectFlowField<List<T>> {

	public RemoveEntriesListField(List<T> key) {
		super(Keys.getProperty(key), true);
	}

	private class RemoveThisObjectAction extends ResourceAction {
		private final T object;
		
		private RemoveThisObjectAction(T object) {
			this.object = object;
		}

		@Override
		public void action(IComponent context) {
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
	protected IForm<List<T>> createFormPanel() {
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
