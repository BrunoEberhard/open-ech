package ch.openech.client;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.resources.ResourceAction;

public class RemoveEntriesListField<T> extends ObjectFlowField<List<T>> {
	private List<T> values = new ArrayList<T>();

	public RemoveEntriesListField(Object key) {
		super(Constants.getConstant(key), true);
	}

	public void setValues(List<T> values) {
		this.values = values;
	}
	
	private class RemoveThisObjectAction extends ResourceAction {
		private final T object;
		
		private RemoveThisObjectAction(T object) {
			this.object = object;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			getObject().remove(object);
			setObject(getObject());
		}
	}

	private class AddAllAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			setObject(new ArrayList<T>(values));
		}
	}

	@Override
	protected FormVisual<List<T>> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void show(List<T> objects) {
		for (T object : objects) {
			addObject(object);
			addAction(new RemoveThisObjectAction(object));
		}
		addGap();
		addAction(new AddAllAction());
	}

}
