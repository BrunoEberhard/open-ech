package ch.openech.client;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.MultiLineObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.resources.ResourceAction;


// TODO !!

public class RemoveEntriesListField<T> extends MultiLineObjectField<List<T>> {

	private List<T> values = new ArrayList<T>();
	private List<T> selectedValues = new ArrayList<T>();

	public RemoveEntriesListField(Object key) {
		super(Constants.getConstant(key), true);
	}

	public void setValues(List<T> values) {
		this.values = values;
		selectedValues.clear();
		selectedValues.addAll(values);
		
		setObject(selectedValues);
	}
	
	@Override
	public List<T> getObject() {
		return selectedValues;
	}

	@Override
	public void setObject(List<T> selectedValues) {
		for (T object : selectedValues) {
			addObject(object);
			addAction(new RemoveSelectedObjectAction(object));
		}
		addGap();
		addAction(new AddAllAction());
	}

	private class RemoveSelectedObjectAction extends ResourceAction {
		private final T object;
		
		private RemoveSelectedObjectAction(T object) {
			this.object = object;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			selectedValues.remove(object);
			setObject(selectedValues);
		}
	}

	private class AddAllAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			selectedValues.clear();
			selectedValues.addAll(values);
			setObject(selectedValues);
		}
	}

	@Override
	protected FormVisual<List<T>> createFormPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void display(List<T> object) {
		// TODO Auto-generated method stub
		
	}

}
